package com.example.scanfood.domain

import android.graphics.Color
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.gson.annotations.Expose
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        LocalDate.parse(parcel.readString()),
        LocalDate.parse(parcel.readString()),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(dateExp.toString())
        parcel.writeString(scanDate.toString())
        parcel.writeByte(if (hide) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}

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
 * Convert to a message depending on date
 *
 * @param
 * @return
 * @see
 */
fun Product.toInfoCategory(): String {
    return when {
        dateExp!!.isAfter(ChronoLocalDate.from(ZonedDateTime.now())) -> {
            "Votre aliment a dépassé la date d'expiration."
        }
        dateExp!!.isBefore(ChronoLocalDate.from(ZonedDateTime.now())) -> {
            "Tout va bien !"
        }
        else -> {
            "Votre aliment est bientôt périmé. Faites attention !"
        }
    }
}
