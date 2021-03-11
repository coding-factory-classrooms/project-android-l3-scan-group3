package com.example.scanfood.infrastructure.api

import android.util.Log
import com.example.scanfood.domain.Product
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val TAG = "ScanFoodService"

interface CustomCallBack{
    fun onProductCallBack(value: Product)
}

interface IScanFood {
    /**
     * Return a raw data json,
     * it will be serialized on the way to a Product
     *
     * @param  id   an integer id
     * @return      the product from id=?
     * @see         Call<Product>
     */
    @GET("/scanfoods/{id}")
    fun getFood(@Path("id") id: Int): Observable<Product>
}

object ScanFoodService {
    private lateinit var gson: Gson
    private lateinit var refrofit: Retrofit
    private lateinit var service: IScanFood
    private lateinit var client: OkHttpClient

    /**
     * Create instances [Gson], [Retrofit], [IScanFood]
     *
     * @param
     * @return
     * @see
     */
    fun init() {
        client = OkHttpClient
            .Builder()
            .build()
        gson = GsonBuilder().setDateFormat("dd/MM/yyyy").create()
        refrofit = Retrofit.Builder()
            .baseUrl("http://15.237.137.24:8080")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        service = refrofit.create(
            IScanFood::class.java
        )
    }

    /**
     * Query finding by Id.
     * It will handle success or failure cases
     *
     * @param  id   an [Integer]
     * @return
     * @see
     */
    fun findById(id: Int, customCallBack: CustomCallBack) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            service.getFood(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({resp -> Log.i(TAG, "food response : ${resp.image}")
                    customCallBack.onProductCallBack(resp)
                }, { err -> Log.d(TAG, "error response : ${err.message}") }))

    }
}

