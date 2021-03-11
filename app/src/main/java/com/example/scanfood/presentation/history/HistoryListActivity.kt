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
    private val products: HistoryListViewModel by viewModels()
    private val api: ScanFoodService =
        ScanFoodService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        products.getState().observe(this, Observer { updateUI(it)})

        adapter =
            HistoryAdapter(listOf())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.fab.setOnClickListener {

        }

        var product: Product

        api.init()
        api.findById(1, object :
            CustomCallBack {
            override fun onProductCallBack(value: Product) {
                product = value
                Log.i(TAG, "onProductCallBack : $product")
//                adapter.updateDataSet(listOf(product))
            }

        })

    }

    private fun updateUI(state: HistoryListViewModelState){
        when(state){
            HistoryListViewModelState.Loading -> TODO()
            HistoryListViewModelState.Empty -> TODO()
            is HistoryListViewModelState.CameraOff -> TODO()
            is HistoryListViewModelState.Failure -> TODO()
            is HistoryListViewModelState.Success -> {
                adapter.updateDataSet(state.products)
            }
        }
    }
}