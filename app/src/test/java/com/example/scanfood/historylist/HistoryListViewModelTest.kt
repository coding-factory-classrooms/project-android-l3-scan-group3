package com.example.scanfood.historylist

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.scanfood.application.history.HistoryListViewModel
import com.example.scanfood.application.history.HistoryListViewModelState
import com.example.scanfood.domain.Product
import com.example.scanfood.infrastructure.database.DataBaseHandler
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.robin.movie.testObserver
import java.time.LocalDate

class HistoryListViewModelTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `init View Model should yields state Empty`() {
        val model = HistoryListViewModel()
        val observer = model.getState().testObserver()

        Assert.assertEquals(
            listOf(
                HistoryListViewModelState.Empty
            ), observer.observedValues
        )
    }

    @Test
    fun `updating data View Model should yields state Changed`() {
        val model = HistoryListViewModel()
        val observer = model.getState().testObserver()

        model.simulateScanWithoutDb()

        Assert.assertEquals(
            listOf(
                HistoryListViewModelState.Empty,
                HistoryListViewModelState.Changed(products = model.getState().value!!.products)
            ), observer.observedValues
        )
    }

    @Test
    fun `database init should success`() {
        val model = HistoryListViewModel()

        val mockContext = Mockito.mock(Context::class.java)
        model.preparingDatabase(mockContext)

        Assert.assertNotNull(model.db)
    }

    @Test
    fun `camera toggling should yields state CameraOff`() {
        val model = HistoryListViewModel()
        val observer = model.getState().testObserver()

        model.toggleCamera()

        Assert.assertEquals(
            listOf(
                HistoryListViewModelState.Empty,
                HistoryListViewModelState.CameraOff(cameraEnabled = false)
            ), observer.observedValues
        )
    }
}