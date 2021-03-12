package com.example.scanfood.presentation.detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.scanfood.databinding.ActivityDetailBinding
import com.example.scanfood.domain.*
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    val picasso = Picasso.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Product>("product")
        Log.i("DetailActivity", "product=${data!!}")

        binding.textViewProduct.text = data.getTitle()
        binding.textViewDate.text = data.getDateExp()
        binding.textViewDate.setBackgroundColor(data.getSetColor())
        binding.textViewInfo.text = data.getSetInfo()
        binding.textViewDDJ.text = data.getScanDate()
        picasso.load(data.getImage()).into(binding.imageViewProduct)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}