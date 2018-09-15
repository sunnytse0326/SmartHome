package com.smarthome.activity

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.work.WorkManager
import com.smarthome.R
import com.smarthome.adapter.HomeRecyclerViewAdapter
import com.smarthome.jobscheduler.SyncWeatherWorker
import com.smarthome.model.Room
import com.smarthome.model.Rooms
import com.smarthome.uicomponent.HomeUI
import com.smarthome.viewmodel.HomeViewModel
import org.jetbrains.anko.setContentView
import java.util.*
import kotlin.concurrent.timerTask


class HomeActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var mainUI: HomeUI
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var rooms: Rooms
    private lateinit var adapter: HomeRecyclerViewAdapter
    private val mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initView()
        setWeatherScheduleJob()
    }

    private fun initView(){
        mainUI.setContentView(this)

        mainUI.recyclerView.layoutManager = LinearLayoutManager(this)
        mainUI.recyclerView.adapter = adapter
    }

    private fun initData(){
        mainUI = HomeUI()
        rooms = Rooms(Room(mutableMapOf()), Room(mutableMapOf()), Room(mutableMapOf()))
        homeViewModel = ViewModelProviders.of(this, HomeViewModel.VMFactory(mLifecycleRegistry, this, this@HomeActivity.applicationContext)).get(HomeViewModel::class.java)

        homeViewModel.getRoomFixtures().observe(this, Observer { rooms ->
            mainUI.loadLty.visibility = View.GONE
            adapter.setRoomData(rooms)
        })

        homeViewModel.getErrors().observe(this, Observer { rooms ->
            mainUI.loadLty.visibility = View.GONE
            Toast.makeText(this@HomeActivity, this@HomeActivity.getString(R.string.error_failed_api), Toast.LENGTH_SHORT).show()
        })

        adapter = HomeRecyclerViewAdapter(this, object : HomeRecyclerViewAdapter.OnClickListener {
            override fun onSwitchChanged(checked: Boolean, url: String) {
                homeViewModel.controlRoomFixtures(url).observe(this@HomeActivity, Observer { rooms ->
                    Toast.makeText(this@HomeActivity, if (checked) this@HomeActivity.getString(R.string.switch_on_msg) else this@HomeActivity.getString(R.string.switch_off_msg), Toast.LENGTH_SHORT).show()
                })
            }
        }, rooms)
    }

    private fun setWeatherScheduleJob(){
        WorkManager.getInstance().cancelAllWorkByTag(SyncWeatherWorker.TAG_SINGLE)
        WorkManager.getInstance().cancelAllWorkByTag(SyncWeatherWorker.TAG_PERIODIC)
        val uuid = SyncWeatherWorker.scheduleWeatherJob()

        WorkManager.getInstance().getStatusById(uuid)
                .observe(this, Observer { workStatus ->
                    if (workStatus?.state?.isFinished ?: false && workStatus?.outputData?.getBooleanArray(SyncWeatherWorker.RESULT_DATA) != null) {
                        val data = (workStatus?.outputData?.getBooleanArray(SyncWeatherWorker.RESULT_DATA)) as BooleanArray
                        if(data != null && data[1]){
                            adapter.rooms.bedRoom?.fixture.put("Light1", data[1])
                            adapter.notifyDataSetChanged()
                            Toast.makeText(this@HomeActivity, if (data[1]) this@HomeActivity.getString(R.string.switch_on_msg) else this@HomeActivity.getString(R.string.switch_off_msg), Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }

    override fun onStop() {
        super.onStop()
        SyncWeatherWorker.schedulePeriodicWeatherJob()
    }
}
