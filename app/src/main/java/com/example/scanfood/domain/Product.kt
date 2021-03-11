package com.example.scanfood.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.google.gson.annotations.Expose
import java.net.URL
import java.util.*

data class Product(
    @Expose
    val id: Int,
    @Expose
    val title: String,
    @Expose
    val image: String,
    @Expose
    val dateExp: Date,
    @Expose(serialize = false, deserialize = false)
    var scanDate: Date,
    @Expose(serialize = false, deserialize = false)
    var hide: Boolean = false
)


fun Product.toColorCategory(): Int {
    return when {
        dateExp.after(Date()) -> {
            Color.RED
        }
        dateExp.before(Date()) -> {
            Color.GREEN
        }
        else -> {
            Color.YELLOW
        }
    }
}

fun Product.toImage(): Bitmap {
    val url = URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464")
    return BitmapFactory.decodeStream(url.openConnection().getInputStream())
}
