package com.example.scanfood.application.history

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scanfood.domain.Product
import com.example.scanfood.domain.toColorCategory
import com.example.scanfood.domain.toInfoCategory
import com.example.scanfood.infrastructure.api.CustomCallBack
import com.example.scanfood.infrastructure.api.ScanFoodService
import com.example.scanfood.infrastructure.database.DataBaseHandler
import com.example.scanfood.infrastructure.database.TABLENAME
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val TAG = "HistoryListViewModel"

sealed class HistoryListViewModelState(
    open val errorMessage: String = "",
    open val cameraEnabled: Boolean = true,
    open val products: List<Product> = listOf()
) {
    object Empty : HistoryListViewModelState(products = listOf())
    data class CameraOff(override val cameraEnabled: Boolean) :
        HistoryListViewModelState(cameraEnabled = cameraEnabled)

    data class Failure(override val errorMessage: String) :
        HistoryListViewModelState(errorMessage = errorMessage)

    data class Changed(override val products: List<Product>) : HistoryListViewModelState()
}

class HistoryListViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    var placeholderProduct: Product =
        Product(
            0,
            "Tarte aux pommes",
            "https://static.750g.com/images/600-600/9823eb627203c878f3e36d72f8ce6d1c/tarte-aux-pommes.jpg",
            LocalDate.now(),
            LocalDate.now()
        )
    lateinit var db: DataBaseHandler
    private val api: ScanFoodService = ScanFoodService
    private var products = mutableListOf<Product>()
    private val state = MutableLiveData<HistoryListViewModelState>()
    fun getState(): LiveData<HistoryListViewModelState> = state

    init {
        api.init()
        state.value = HistoryListViewModelState.Empty
        products.clear()

    }

    fun simulateIsActive(): Boolean = !state.value!!.cameraEnabled

    fun onFetchQrData(id: Int) {
        api.findById(id, object :
            CustomCallBack {
            override fun onProductCallBack(value: Product) {
                val p = products
                    .filter { prod -> prod.id == value.id }
                    .getOrNull(0)
                if (p == null) addItem(value, db) else updateItem(products.indexOf(value), value, db)
                Log.i(com.example.scanfood.presentation.history.TAG, "onProductCallBack : $value")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun simulateScan() {
        usePlaceHolderData()
        Log.d(TAG, "simulate data without camera")
    }

    fun simulateScanWithoutDb(){
        products.add(placeholderProduct)
        state.postValue(HistoryListViewModelState.Changed(products = products))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun usePlaceHolderData() {
        if (!products.contains(placeholderProduct)) addItem(placeholderProduct, db)
    }

    fun preparingDatabase(context: Context) {
        db = DataBaseHandler(context)
//        db.clearDatabase(TABLENAME)
    }

    fun toggleCamera() {
        state.postValue(HistoryListViewModelState.CameraOff(cameraEnabled = !state.value!!.cameraEnabled))
    }

    fun clearData() {
        products = mutableListOf()
        state.postValue(HistoryListViewModelState.Empty)
    }

    fun refresh(db: DataBaseHandler) {
        val prods = db.getAllProduct();
        products.clear()
        products.addAll(prods)
        state.postValue(HistoryListViewModelState.Changed(products = products))
        Log.i(TAG, "products fetched")
    }

    fun getItems(db: DataBaseHandler) {
        val prods = db.getAllProduct();
        products.addAll(prods)
        state.postValue(HistoryListViewModelState.Changed(products = prods))
        Log.i(TAG, "products fetched")
    }

    fun addItem(product: Product, db: DataBaseHandler) {
        product.scanDate = LocalDate.now()
        products.add(product)
        val done = db.addProduct(product)
        if (done.toString() != (-1).toString()) {
            state.postValue(HistoryListViewModelState.Changed(products = products))
        }
        Log.i(TAG, "product added")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateItem(index: Int, product: Product, db: DataBaseHandler) {
        product.scanDate = LocalDate.now()
        products[index] = product
        val isDone = db.updateProduct(product)
        if (isDone) {
            state.postValue(HistoryListViewModelState.Changed(products = products))
        }
        Log.i(TAG, "product updated")
    }

    fun deleteItem(product: Product, db: DataBaseHandler) {
        products.remove(product)
        val isDone = db.deleteProduct(product.id)
        if (isDone) {
            state.postValue(HistoryListViewModelState.Changed(products = products))
        }
        Log.w(TAG, "product deleted")
    }


    fun orderByDate() {
        refresh(db)
        products.sortBy { it.dateExp }
        state.postValue(HistoryListViewModelState.Changed(products = products))
        Log.i(TAG, "products now ordered by expiration date")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterByColorDuration(color: Int) {
        refresh(db)
        products.removeIf {it.toColorCategory() != color }
        state.postValue(HistoryListViewModelState.Changed(products = products))
        Log.i(TAG, "products now filtered by duration color")
    }

}