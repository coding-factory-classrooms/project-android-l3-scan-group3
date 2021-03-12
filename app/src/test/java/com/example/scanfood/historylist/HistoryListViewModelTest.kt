package com.example.scanfood.historylist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.scanfood.application.history.HistoryListViewModel
import org.junit.Rule
import org.junit.Test
import org.robin.movie.testObserver

class HistoryListViewModelTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `correct simulation scan yields state Changed`(){
        val model = HistoryListViewModel()
        val observer = model.getState().testObserver()

    }
}