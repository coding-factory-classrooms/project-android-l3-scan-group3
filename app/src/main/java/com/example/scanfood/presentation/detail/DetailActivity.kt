package com.example.scanfood.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.scanfood.application.history.HistoryListViewModel
import com.example.scanfood.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val myProduct: HistoryListViewModel by viewModels()
    val picasso = Picasso.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewProduct.text = myProduct.getTitle()
        binding.textViewDate.text = myProduct.getDateExp()
        binding.textViewDate.setBackgroundColor(myProduct.getSetColor())
        binding.textViewInfo.text = myProduct.getSetInfo()
        binding.textViewDDJ.text = myProduct.getScanDate()
        picasso.load(myProduct.getImage()).into(binding.imageViewProduct)

    }
}