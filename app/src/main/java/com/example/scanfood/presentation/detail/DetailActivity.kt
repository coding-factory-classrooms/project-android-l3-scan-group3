package com.example.scanfood.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.scanfood.R
import com.example.scanfood.application.history.HistoryListViewModel
import com.example.scanfood.databinding.ActivityDetailBinding
import com.example.scanfood.databinding.ActivityHistoryListBinding
import com.example.scanfood.domain.Product
import com.example.scanfood.infrastructure.api.ScanFoodService

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val myProducts: HistoryListViewModel by viewModels()
    private val api: ScanFoodService =
        ScanFoodService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewProduct.text = myProducts.getTitle()
        binding.textViewDate.text = myProducts.getDateExp().toString()
       // binding.imageViewProduct.imageAlpha = myProducts.getImage()



    }
}