
package com.example.scanfood.presentation.history

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanfood.R
import com.example.scanfood.presentation.scan.ScanActivity
import com.example.scanfood.application.history.HistoryListViewModel
import com.example.scanfood.application.history.HistoryListViewModelState
import com.example.scanfood.databinding.ActivityHistoryListBinding
import com.example.scanfood.domain.Product
import com.example.scanfood.infrastructure.api.ScanFoodService
import com.example.scanfood.presentation.detail.DetailActivity


const val TAG = "HistoryActivity"

class HistoryListActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    private lateinit var binding: ActivityHistoryListBinding
    private lateinit var adapter: HistoryAdapter
    private val model: HistoryListViewModel by viewModels()
    private val api: ScanFoodService = ScanFoodService
    private val LAUNCH_SECOND_ACTIVITY: Int = 1
    val colors = arrayOf("green", "red", "yellow")

    /**
     * Create all requirements
     * for the activity
     *
     * Associate [binding], [adapter], [model], [api]
     *
     * Prepare listeners for the FAB
     * camera off = simulation
     * camera on = scan
     *
     * @param savedInstanceState Bundle?
     * @return
     * @see
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = HistoryAdapter(listOf(), this, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.apply { addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)) }
        model.preparingDatabase(this)
        model.getItems(model.db)
        model.getState().observe(this, Observer { updateUI(it)})
        val search = binding.searchView
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, colors)


        binding.fab.setOnClickListener {
            if(model.simulateIsActive()) model.simulateScan() else navigateToScan()
        }
        binding.fab.setOnLongClickListener {
            model.toggleCamera()
            true
        }


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                if(colors.contains(query)) {
                    colorsTri(query)
                   // adapter1.filter.filter(query)

                } else {
                    Toast.makeText(applicationContext, "Color not found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                colorsTri(newText)
                return false
            }

        })
    }

    /**
     * Navigate to Detail activity
     *
     * @param product Product
     * @return
     * @see
     */
    fun navigateToDetail(product: Product){
        val detailIntent = Intent(this@HistoryListActivity, DetailActivity::class.java)
        detailIntent.action = Intent.ACTION_VIEW
        detailIntent.putExtra("product", product)
        startActivity(detailIntent)
    }

    /**
     * Navigate to Scan activity
     *
     * @param product Product
     * @return
     * @see
     */
    fun navigateToScan(){
        val scanIntent = Intent(this@HistoryListActivity, ScanActivity::class.java)
        scanIntent.action = Intent.ACTION_VIEW
        startActivityForResult(scanIntent, LAUNCH_SECOND_ACTIVITY)
    }

    /**
     * Updating user interface depending on
     * [HistoryListViewModelState]
     *
     * @param state HistoryListViewModelState
     * @return Bitmap
     * @see
     */
    private fun updateUI(state: HistoryListViewModelState){
        when(state){
            HistoryListViewModelState.Empty -> {
                Log.i(TAG, "updateUI : empty list")
            }
            is HistoryListViewModelState.CameraOff -> {
                Toast.makeText(this@HistoryListActivity, "Camera is now ${ if(state.cameraEnabled) "actived" else "disabled"}", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "updateUI : camera=${state.cameraEnabled}")
            }
            is HistoryListViewModelState.Changed -> {
                Log.i(TAG, "updateUI : changed, state=$state")
                adapter.updateDataSet(state.products)
            }
        }
    }

    /**
     * On navigation back to this activity,
     * if Activity get data, will call API
     *
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent?
     * @return
     * @see
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                val result: String? = data!!.getStringExtra("id")
                model.onFetchQrData(result!!.toInt())
                Log.i(TAG, "RESULT = $result")
                if (resultCode == Activity.RESULT_CANCELED) {
                    Log.i(TAG, "onActivityResult=$resultCode")
                }
            }
        }
    }

    /**
     * Interaction on long click,
     * will delete a product
     *
     * @param v: View
     * @return
     * @see
     */
    override fun onLongClick(v: View): Boolean {
        if(v.tag is Product){
            val product: Product = v.tag as Product
            Log.i(TAG, "onClickListener=$product")
            model.deleteItem(product, model.db)
        }
        return true
    }

    /**
     * Interaction on long click,
     * will navigate to a detail of a product
     *
     * @param v: View
     * @return
     * @see
     */
    override fun onClick(v: View) {
        if(v.tag is Product){
            val product: Product = v.tag as Product
            navigateToDetail(product)
            Log.i(TAG, "onClickListener=$product")
        }
    }

    fun colorsTri(text: String?) {
        when(text) {
            "red" -> model.filterByColorDuration(-65536) //color int red
            "green" -> model.filterByColorDuration(-16711936) // color int green
            "yellow" -> model.filterByColorDuration(-256) //color int yellow
        }
    }


}