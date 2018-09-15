package com.smarthome.activity

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.evernote.android.job.JobManager
import com.smarthome.R
import com.smarthome.adapter.HomeRecyclerViewAdapter
import com.smarthome.jobscheduler.JobScheduleCreator
import com.smarthome.jobscheduler.SyncWeatherJob
import com.smarthome.model.Room
import com.smarthome.model.Rooms
import com.smarthome.uicomponent.HomeUI
import com.smarthome.viewmodel.HomeViewModel
import org.jetbrains.anko.setContentView

class HomeActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var mainUI: HomeUI
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var rooms: Rooms
    private lateinit var adapter: HomeRecyclerViewAdapter
    private val mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainUI = HomeUI()
        mainUI.setContentView(this)

        homeViewModel = ViewModelProviders.of(this, HomeViewModel.VMFactory(mLifecycleRegistry, this)).get(HomeViewModel::class.java)

        homeViewModel.getRoomFixtures().observe(this, Observer { rooms ->
            mainUI.loadLty.visibility = View.GONE
            adapter.setRoomData(rooms)
        })

        homeViewModel.getErrors().observe(this, Observer { rooms ->
            mainUI.loadLty.visibility = View.GONE
            Toast.makeText(this@HomeActivity, this@HomeActivity.getString(R.string.error_failed_api), Toast.LENGTH_SHORT).show()
        })

        rooms = Rooms(Room(mutableMapOf()), Room(mutableMapOf()), Room(mutableMapOf()))

        adapter = HomeRecyclerViewAdapter(object : HomeRecyclerViewAdapter.OnClickListener {
        adapter = HomeRecyclerViewAdapter(this, object : HomeRecyclerViewAdapter.OnClickListener {
            override fun onSwitchChanged(checked: Boolean, url: String) {
                homeViewModel.controlRoomFixtures(url).observe(this@HomeActivity, Observer { rooms ->
                    Toast.makeText(this@HomeActivity, if (checked) this@HomeActivity.getString(R.string.switch_on_msg) else this@HomeActivity.getString(R.string.switch_off_msg), Toast.LENGTH_SHORT).show()
                })
            }
        }, rooms)

        mainUI.recyclerView.layoutManager = LinearLayoutManager(this)
        mainUI.recyclerView.adapter = adapter

        JobManager.create(this).addJobCreator(JobScheduleCreator())

        SyncWeatherJob.scheduleAdvancedJob()
    }
}
