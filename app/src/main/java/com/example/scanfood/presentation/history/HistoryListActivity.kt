package com.example.scanfood.presentation.history

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanfood.databinding.ActivityHistoryListBinding
import com.example.scanfood.domain.Product
import com.example.scanfood.application.history.HistoryListViewModel
import com.example.scanfood.application.history.HistoryListViewModelState
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = HistoryAdapter(listOf())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        model.getState().observe(this, Observer { updateUI(it)})
        api.init()

        binding.fab.setOnClickListener {
            model.scan()

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
                Log.i(TAG, "updateUI : loading...")
            }
            HistoryListViewModelState.Empty -> {
                Log.i(TAG, "updateUI : empty list")
            }
            is HistoryListViewModelState.CameraOff -> {
                Log.i(TAG, "updateUI : camera=${state.cameraEnabled}")
            }
            is HistoryListViewModelState.Failure -> {
                Log.d(TAG, "updateUI : failure, err=${state.errorMessage}")
            }
            is HistoryListViewModelState.Changed -> {
                Log.i(TAG, "updateUI : changed, state=$state")
                adapter.updateDataSet(state.products)
            }
        }
    }
}