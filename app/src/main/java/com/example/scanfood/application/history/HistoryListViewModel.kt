package com.example.scanfood.application.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scanfood.domain.Product
import java.util.*

const val TAG = "HistoryListViewModel"

sealed class HistoryListViewModelState(
    open val errorMessage: String = "",
    open val cameraEnabled: Boolean = true,
    open val products: List<Product> = listOf()
) {
    object Loading : HistoryListViewModelState()
    object Empty : HistoryListViewModelState(products = listOf())
    data class CameraOff(override val cameraEnabled: Boolean) : HistoryListViewModelState(cameraEnabled = cameraEnabled)
    data class Failure(override val errorMessage: String) :
        HistoryListViewModelState(errorMessage = errorMessage)
    data class Changed(override val products: List<Product>) : HistoryListViewModelState()
}



class HistoryListViewModel : ViewModel() {
    private var placeholderProduct: Product =
        Product(
            0,
            "Tarte aux pommes",
            "https://static.750g.com/images/600-600/9823eb627203c878f3e36d72f8ce6d1c/tarte-aux-pommes.jpg",
            Date(),
            Date()
        )
    private var products = mutableListOf<Product>()
    private val state = MutableLiveData<HistoryListViewModelState>()
    fun getState(): LiveData<HistoryListViewModelState> = state
    init { state.value = HistoryListViewModelState.Empty }


    fun scan(){
        //TODO : implements
        if(!state.value!!.cameraEnabled){
            usePlaceHolderData()
            Log.d(TAG, "simulate data wihout camera")
        }
        Log.i(TAG, "scanning...")

        // do something
    }

    fun getQrData(){
        //TODO : implements
    }

    fun toggleCamera(){
        state.postValue(HistoryListViewModelState.CameraOff(cameraEnabled = !state.value!!.cameraEnabled))
    }


    private fun usePlaceHolderData(){
        if(!products.contains(placeholderProduct)) addItem(placeholderProduct)
    }

    fun clearData(){
        products = mutableListOf()
        state.postValue(HistoryListViewModelState.Empty)
    }

    fun fetchDataByIdFromApi(){
        //TODO : implements

    }

    fun getItems(){
        //TODO : implements
    }

    fun addItem(product: Product){
        products.add(product)
        state.postValue(HistoryListViewModelState.Changed(products = products))
        Log.i(TAG, "product added")
    }

    fun updateItem(index: Int, product: Product){
        product.scanDate = Date()
        products[index] = product
        state.postValue(HistoryListViewModelState.Changed(products = products))
        Log.i(TAG, "product updated")
    }

    fun deleteItem(index: Int, product: Product){
        products.remove(product)
        state.postValue(HistoryListViewModelState.Changed(products = products))
        Log.w(TAG, "product deleted")
    }

    fun orderByDate(){
        products.sortedBy { it.dateExp }
        state.postValue(HistoryListViewModelState.Changed(products = products))
        Log.i(TAG, "products now ordered by expiration date")
    }

    fun filterByColorDuration(color: Int){
        products.map { if(it.toColorCategory() != color ) it.hide = true else it.hide }
        state.postValue(HistoryListViewModelState.Changed(products = products))
        Log.i(TAG, "products now filtered by duration color")
    }
}