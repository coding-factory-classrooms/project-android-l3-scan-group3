package com.example.scanfood.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.Expose
import java.net.URL
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.chrono.ChronoLocalDate

data class Product(
    @Expose
    val id: Int,
    @Expose
    val title: String,
    @Expose
    val image: String,
    @Expose
    val dateExp: LocalDate?,
    @Expose(serialize = false, deserialize = false)
    var scanDate: LocalDate?,
    @Expose(serialize = false, deserialize = false)
    var hide: Boolean = false
)

/**
 * Convert to Color according to date
 *
 * @param
 * @return Int
 * @see
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Product.toColorCategory(): Int {
    return when {
        dateExp!!.isAfter(ChronoLocalDate.from(ZonedDateTime.now())) -> {
            Color.RED
        }
        dateExp!!.isBefore(ChronoLocalDate.from(ZonedDateTime.now())) -> {
            Color.GREEN
        }
        else -> {
            Color.rgb(255, 165, 0)
        }
    }
}

/**
 * Convert url image to Bitmap image
 *
 * @param
 * @return Bitmap
 * @see
 */
fun Product.toImage(): Bitmap {
    return BitmapFactory.decodeStream(URL(image).openStream())
}
