package com.example.scanfood.historylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scanfood.domain.Product
import com.example.scanfood.domain.toColorCategory
import java.util.*


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
    data class Success(override val products: List<Product>) : HistoryListViewModelState()
}



class HistoryListViewModel : ViewModel() {


    private val state = MutableLiveData<HistoryListViewModelState>()
    fun getState(): LiveData<HistoryListViewModelState> = state

    fun routerCamera(){
        //TODO : implements
    }

    fun scanQr(){
        //TODO : implements
    }

    fun getQrData(){
        //TODO : implements
    }

    fun toggleCamera(){
        state.postValue(HistoryListViewModelState.CameraOff(!state.value!!.cameraEnabled))
    }

    fun usePlaceHolderData(){
        val product: Product =
            Product(
                0,
                "Tarte aux pommes",
                "https://static.750g.com/images/600-600/9823eb627203c878f3e36d72f8ce6d1c/tarte-aux-pommes.jpg",
                Date(),
                Date()
            )
        addItem(product)
    }

    fun clearData(){
        state.postValue(HistoryListViewModelState.Empty)
    }

    fun fetchDataByIdFromApi(){
        //TODO : implements

    }

    fun getItems(){
        //TODO : implements
    }

    fun addItem(product: Product){
        val adding: List<Product> = state.value!!.products.plus(product)
        state.postValue(HistoryListViewModelState.Success(products = adding))
    }

//    fun updateItem(id: Int){
//        val next: List<Product> = state.value!!.products.map { item -> {
//            if (item.id == id) item.scanDate = Date() else item
//        } } as List<Product>
//        state.postValue(HistoryListViewModelState.Success(products = next))
//    }

    fun deleteItem(product: Product){
        val next: List<Product> = state.value!!.products.minus(product)
        state.postValue(HistoryListViewModelState.Success(products = next))
    }
    fun orderByDate(){
        val next: List<Product> = state.value!!.products.sortedBy { it.dateExp }
        state.postValue(HistoryListViewModelState.Success(products = next))
    }

//    fun filterByColorDuration(color: Int){
//        val next: List<Product> = state.value!!.products.map {
//            if(it.toColorCategory() != color ) it.hide = true else it.hide
//        }  as List<Product>
//        state.postValue(HistoryListViewModelState.Success(products = next))
//    }

    fun onFABLongPressed(){
        toggleCamera()
    }
}