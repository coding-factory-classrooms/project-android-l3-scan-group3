package com.example.scanfood.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.scanfood.application.history.HistoryListViewModel
import com.example.scanfood.databinding.ActivityDetailBinding
import com.example.scanfood.domain.Product
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val model: HistoryListViewModel by viewModels()
    val picasso = Picasso.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Product>("product")
        Log.i("DetailActivity", "product=${data!!}")

        binding.textViewProduct.text = model.getTitle(data)
        binding.textViewDate.text = model.getDateExp(data)
        binding.textViewDate.setBackgroundColor(model.getSetColor(data))
        binding.textViewInfo.text = model.getSetInfo(data)
        binding.textViewDDJ.text = model.getScanDate(data)
        picasso.load(model.getImage(data)).into(binding.imageViewProduct)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}