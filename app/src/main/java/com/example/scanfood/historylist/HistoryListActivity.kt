package com.example.scanfood.historylist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanfood.databinding.ActivityHistoryListBinding
import com.example.scanfood.infrastructure.ScanFoodService

class HistoryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryListBinding
    private lateinit var adapter: HistoryAdapter
    private val products: HistoryListViewModel by viewModels()
    private val api: ScanFoodService = ScanFoodService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        products.getState().observe(this, Observer { updateUI(it)})

        adapter = HistoryAdapter(listOf())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.fab.setOnClickListener { products.toggleCamera() }

        api.init()
        api.findById(2)

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