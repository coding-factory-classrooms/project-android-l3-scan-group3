
package com.example.scanfood.presentation.history

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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


const val TAG = "HistoryActivity"

class HistoryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryListBinding
    private lateinit var adapter: HistoryAdapter
    private val model: HistoryListViewModel by viewModels()
    private val api: ScanFoodService = ScanFoodService


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
        adapter = HistoryAdapter(listOf())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.apply { addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)) }
        model.getState().observe(this, Observer { updateUI(it)})

        api.init()


//        val intent = Intent(this@HistoryListActivity, ScanActivity::class.java)
//        intent.action = Intent.ACTION_VIEW
//        intent.addCategory()

        binding.fab.setOnClickListener {
            if(model.simulateIsActive()) model.simulateScan() else startActivity(intent)
        }
        binding.fab.setOnLongClickListener {
            model.toggleCamera()
            true
        }


        // to change , the id need to come from the scan result
        api.findById(1, object :
            CustomCallBack {
            override fun onProductCallBack(value: Product) {
                Log.i(TAG, "onProductCallBack : $value")
                model.addItem(value)
            }

        })

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

}