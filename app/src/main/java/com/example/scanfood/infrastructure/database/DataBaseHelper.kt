package com.example.scanfood.infrastructure.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DATABASENAME = "MY DATABASE"
val TABLENAME = "MyProduct"
val COL_ID = ""
val COL_NAME = ""
val COL_IMAGE = ""
val COL_DATEEXP = ""
val COL_SCANDATE = ""
val COL_USERNAME = ""

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context,
    DATABASENAME, null,
    1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLENAME + " (" + COL_ID + " INTEGER PRIMARY KEY," + COL_NAME + " VARCHAR(256)," + COL_IMAGE + "VARCHAR(256)" + COL_DATEEXP + "DATE" + COL_SCANDATE + " DATE" + COL_USERNAME + "VARCHAR(256))"
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }

}