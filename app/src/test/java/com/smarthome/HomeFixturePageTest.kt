package com.smarthome

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.content.Context
import android.test.mock.MockContext
import com.smarthome.viewmodel.HomeViewModel
import org.junit.Test

import org.junit.Assert.*
import android.arch.lifecycle.Lifecycle
import com.github.kittinunf.fuel.core.FuelManager
import com.smarthome.model.Rooms
import com.smarthome.repository.BaseRepository
import org.mockito.Mockito.*


class HomeFixturePageTest {
    private val lifecycleOwner = mock(LifecycleOwner::class.java)
    private val lifecycle = LifecycleRegistry(lifecycleOwner)
    private val context: Context = MockContext()
    private val homeViewModel: HomeViewModel = HomeViewModel(lifecycle, mockLifecycleOwner(), context)

    @Test
    fun testRepositorySettings() {
        assertEquals("http://private-1e863-house4591.apiary-mock.com/", FuelManager.instance.basePath)
        assertEquals(4 * 60 * 60 * 1000, BaseRepository.CACHETIME)
    }

    @Test
    fun testHomeViewModel() {
        val mockRoom = mock(Rooms::class.java)
        val observer:Observer<Rooms> = mock(Observer::class.java) as Observer<Rooms>
        homeViewModel.getRoomFixtures().observe(lifecycleOwner, observer)
        assertNotNull(observer.onChanged(mockRoom))
    }

    private fun mockLifecycleOwner(): LifecycleOwner {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        doReturn(lifecycle).`when`(lifecycleOwner).lifecycle
        return lifecycleOwner
    }
}
