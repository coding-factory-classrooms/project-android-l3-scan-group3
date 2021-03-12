package com.example.scanfood.infrastructure.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.scanfood.domain.Product
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

val DATABASENAME = "MY DATABASE"
val TABLENAME = "MyProduct"
val COL_ID = "id" //product.id
val COL_NAME = "title" //product.title
val COL_IMAGE = "image"//product.image
val COL_DATEEXP = "dateExp"//product.dateExp
val COL_SCANDATE = "scanDate"//product.scanDate
val COL_USERNAME = "username"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(
    context,
    DATABASENAME, null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLENAME + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_NAME + " VARCHAR(256)," + COL_IMAGE + " VARCHAR(256)," + COL_DATEEXP + " VARCHAR(256)," + COL_SCANDATE + " VARCHAR(256)," + COL_USERNAME + " VARCHAR(256))"
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }

    fun clearDatabase(TABLE_NAME: String) {
        val db = this.writableDatabase
        val clearDBQuery = "DELETE FROM $TABLE_NAME"
        db.execSQL(clearDBQuery)
    }


    private fun getDateToString(date: LocalDate?): String? {
        if (date == null){
            return ""
        }
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return date.format(dateFormat)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getStringToDate(string: String): LocalDate {
        if(string.isEmpty()){
            return LocalDate.now()
        }
        val dateTime = LocalDate.parse(string, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return dateTime
    }

    fun addProduct(product: Product): Long {

        val db = this.writableDatabase
        val values = ContentValues()
        // mettre 0 pour l'id pour l'autoincrement
        values.put(COL_NAME, product.title)
        values.put(COL_IMAGE, product.image)
        values.put(COL_DATEEXP, getDateToString(product.dateExp))
        values.put(COL_SCANDATE, getDateToString(product.scanDate))

        val _success = db.insert(TABLENAME, null, values)
        db.close()
        Log.i("HistoryActivity", product.toString() + " Product added $_success ")


        return _success
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllProduct(): MutableList<Product> {
        var product: Product

        val list: MutableList<Product> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                 product = Product(
                     Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_ID))),
                     cursor.getString(cursor.getColumnIndex(COL_NAME)),
                     cursor.getString(cursor.getColumnIndex(COL_IMAGE)),
                     getStringToDate(cursor.getString(cursor.getColumnIndex(COL_DATEEXP))),
                     getStringToDate(cursor.getString(cursor.getColumnIndex(COL_SCANDATE)))
                 )
                list.add(product)
            }
            while (cursor.moveToNext())
        }
        Log.i("DatabaseHelper", "getAllProduct"  + list)
        return list
    }


    fun updateProduct(product: Product): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_SCANDATE, getDateToString(product.scanDate))
        val _success = db.update(TABLENAME, values, COL_ID + "=?", arrayOf(product.id.toString()))
        db.close()
        Log.i("DatabaseHelper", "updateProduct: $_success")
        return Integer.parseInt("$_success") != -1
    }

    fun deleteProduct(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLENAME, COL_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }
}