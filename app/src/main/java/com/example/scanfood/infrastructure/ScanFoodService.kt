package com.example.scanfood.infrastructure

import android.util.Log
import com.example.scanfood.domain.Product
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val TAG = "ScanFoodService"

private interface IScanFood{
    /**
     * Return a raw data json,
     * it will be serialized on the way to a Product
     *
     * @param  id   an integer id
     * @return      the product from id=?
     * @see         Call<Product>
     */
    @GET("/scanfoods/{id}")
    fun getFood(@Path("id") id: Int): Call<Product>
}


object ScanFoodService{

    private lateinit var gson: Gson
    private lateinit var refrofit: Retrofit
    private lateinit var service: IScanFood

    /**
     * Create instances [Gson], [Retrofit], [IScanFood]
     *
     * @param
     * @return
     * @see
     */
    fun init(){
        gson = GsonBuilder().setDateFormat("dd/MM/yyyy").create()
        refrofit = Retrofit.Builder()
            .baseUrl("http://15.237.137.24:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        service = refrofit.create(
            IScanFood::class.java)
    }

    /**
     * Query finding by Id.
     * It will handle success or failure cases
     *
     * @param  id   an [Integer]
     * @return
     * @see
     */
    fun findById(id: Int){
        service.getFood(id).enqueue(object: Callback<Product> {
            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d(TAG, "error response : ${t.message}")
            }

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                val data = response.body()
                Log.i(TAG, "food response : ${data?.image}")
            }

        })
    }
}

