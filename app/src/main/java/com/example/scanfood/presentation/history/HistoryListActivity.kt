
package com.example.scanfood.presentation.history

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanfood.ScanActivity
import com.example.scanfood.application.history.HistoryListViewModel
import com.example.scanfood.application.history.HistoryListViewModelState
import com.example.scanfood.databinding.ActivityHistoryListBinding
import com.example.scanfood.domain.Product
import com.example.scanfood.infrastructure.api.CustomCallBack
import com.example.scanfood.infrastructure.api.ScanFoodService
import com.example.scanfood.presentation.detail.DetailActivity


const val TAG = "HistoryActivity"

class HistoryListActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    private lateinit var binding: ActivityHistoryListBinding
    private lateinit var adapter: HistoryAdapter
    private val model: HistoryListViewModel by viewModels()
    private val api: ScanFoodService = ScanFoodService
    private val LAUNCH_SECOND_ACTIVITY: Int = 1

    /**
     * Create all requirements
     * for the activity
     *
     * Associate [binding], [adapter], [model], [api]
     *
     * Prepare listeners for the FAB
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
        model.getState().observe(this, Observer { updateUI(it)})

        api.init()


        val scanIntent = Intent(this@HistoryListActivity, ScanActivity::class.java)
        intent.action = Intent.ACTION_VIEW







        // to change , the id need to come from the scan result
        api.findById(1, object :
            CustomCallBack {
            override fun onProductCallBack(value: Product) {
                Log.i(TAG, "onProductCallBack : $value")
                model.addItem(value)
                navigateToScan()
            }

        })
        binding.fab.setOnClickListener {
            if(model.simulateIsActive()) model.simulateScan() else startActivity(intent)
        }
        binding.fab.setOnLongClickListener {
            model.toggleCamera()
            true
        }


    }


    fun navigateToDetail(){
        val detailIntent = Intent(this@HistoryListActivity, DetailActivity::class.java)
        detailIntent.action = Intent.ACTION_VIEW
        detailIntent.putExtra("product", model.placeholderProduct)
        startActivity(detailIntent)
    }

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
            HistoryListViewModelState.Loading -> {
                Toast.makeText(this@HistoryListActivity, "Loading...", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "updateUI : loading...")
            }
            HistoryListViewModelState.Empty -> {
                Log.i(TAG, "updateUI : empty list")
            }
            is HistoryListViewModelState.CameraOff -> {
                Toast.makeText(this@HistoryListActivity, "Camera is now ${ if(state.cameraEnabled) "actived" else "disabled"}", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "updateUI : camera=${state.cameraEnabled}")
            }
            is HistoryListViewModelState.Failure -> {
                Toast.makeText(this@HistoryListActivity, "Something wrong occured...", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "updateUI : failure, err=${state.errorMessage}")
            }
            is HistoryListViewModelState.Changed -> {
                Log.i(TAG, "updateUI : changed, state=$state")
                adapter.updateDataSet(state.products)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                val result: String? = data!!.getStringExtra("id")
                Log.i(TAG, "RESULT = $result")
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if(v?.tag != null)
            Log.i(TAG, "test long click")
        return true
    }

    override fun onClick(v: View?) {
        if(v?.tag != null)
            Log.i(TAG, "test click")
    }

}