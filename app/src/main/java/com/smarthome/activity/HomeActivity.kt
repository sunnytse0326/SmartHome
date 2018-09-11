package com.smarthome.activity

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.smarthome.uicomponent.HomeUI
import com.smarthome.viewmodel.HomeViewModel
import org.jetbrains.anko.setContentView

class HomeActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var mainUI: HomeUI
    private lateinit var homeViewModel: HomeViewModel

    private val mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainUI = HomeUI()
        mainUI.setContentView(this)

        homeViewModel = ViewModelProviders.of(this, HomeViewModel.VMFactory(mLifecycleRegistry, this)).get(HomeViewModel::class.java)

        homeViewModel.getRoomFixtures().observe(this, Observer { rooms ->
            mainUI.loadLty.visibility = View.GONE
        })





    }
}
