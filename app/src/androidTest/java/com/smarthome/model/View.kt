package com.smarthome.model

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.CoordinatesProvider
import android.support.test.espresso.action.GeneralClickAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Tap
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.smarthome.SmartHomeThread

open class View {
    private val TIME_OUT = 30000

    fun onClick(elementId: Int) {
        onView(withId(elementId)).perform(click())
    }

    @Throws(Throwable::class)
    fun onClick(textString: String) {
        onView(withText(textString)).perform(click())
    }

    @JvmOverloads
    fun checkExistence(elementId: Int, currentTry: Int = 0) {
        val EACH_TRY = 500

        if (currentTry >= TIME_OUT / EACH_TRY) {
            onView(withId(elementId)).check(matches(isDisplayed()))
        } else {
            try {
                onView(withId(elementId)).check(matches(isDisplayed()))
            } catch (e: NoMatchingViewException) {
                waitTime(EACH_TRY.toLong())
                checkExistence(elementId)
            }

        }
    }

    @JvmOverloads
    fun checkExistence(text: String, currentTry: Int = 0) {
        val EACH_TRY = 500
        try {
            if (currentTry < TIME_OUT / EACH_TRY) {
                onView(withText(text)).check(matches(isDisplayed()))
            }
        } catch (e: NoMatchingViewException) {
            waitTime(EACH_TRY.toLong())
            checkExistence(text, currentTry + 1)
        }

    }

    private fun waitTime(time: Long) {
        val idlingResource = SmartHomeThread(time)
        Espresso.registerIdlingResources(idlingResource)
    }

    fun clickXY(x: Int, y: Int): ViewAction {
        return GeneralClickAction(
                Tap.SINGLE,
                CoordinatesProvider { view ->
                    val screenPos = IntArray(2)
                    view.getLocationOnScreen(screenPos)

                    val screenX = (screenPos[0] + x).toFloat()
                    val screenY = (screenPos[1] + y).toFloat()

                    floatArrayOf(screenX, screenY)
                },
                Press.FINGER)
    }

}