package com.dk.newssync.presentation.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dk.newssync.data.Result
import com.dk.newssync.data.entity.Entry
import com.dk.newssync.data.usecase.EntriesUseCase
import com.dk.newssync.presentation.common.State
import com.dk.newssync.presentation.ui.DispatcherRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Dimitris Konomis (konomis.dimitris@gmail.com) on 05/03/2019.
 */

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val dispatcherRule: TestRule = DispatcherRule()

    @Mock
    lateinit var entriesUseCase: EntriesUseCase

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        mainViewModel = MainViewModel(entriesUseCase)
    }

    @Test
    fun testGetSelectedThenStateSuccess() = runBlocking {
        val entry = Entry(id = 1)
        val result = Result.success(entry)
        val observer: Observer<State<Entry?>> = mock()

        whenever(entriesUseCase.getSelected()).doReturn(result)

        mainViewModel.selectedEntry.observeForever(observer)
        mainViewModel.findSelected().join()

        verify(entriesUseCase).getSelected()
        verify(observer).onChanged(State.success(entry))
    }

    @Test
    fun testGetSelectedThenStateError() = runBlocking {
        val error = Exception("Unknown Error")
        val result = Result.error(error)
        val observer: Observer<State<Entry?>> = mock()

        whenever(entriesUseCase.getSelected()).doReturn(result)

        mainViewModel.selectedEntry.observeForever(observer)
        mainViewModel.findSelected().join()

        verify(entriesUseCase).getSelected()
        verify(observer).onChanged(State.error(error.localizedMessage, error))
    }

}