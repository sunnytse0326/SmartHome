package com.smarthome.model

import android.support.test.espresso.Espresso
import android.support.test.rule.ActivityTestRule
import com.smarthome.SmartHomePageRoute
import com.smarthome.SmartHomeThread
import com.smarthome.testcase.HomePage
import java.util.HashMap

open class Page : View() {
    private var pageName: String? = null
    private var pageHash: HashMap<String, Page>? = null

    val idxPage: Page?
        get() = pageHash!![pageName] ?: null

    private fun initPageHash() {
        pageHash = object : HashMap<String, Page>() {
            init {
                put("Home", HomePage())
            }
        }
    }

    fun startApp() {
        initPageHash()
    }


    @Throws(Throwable::class)
    fun checkPageExistence(name: String) {
        pageName = name

        val result = SmartHomePageRoute().getPageElementId(name)
        checkExistence(result)
    }

    fun waitTime(time: Long) {
        val idlingResource = SmartHomeThread(time)
        Espresso.registerIdlingResources(idlingResource)
    }

}