package com.smarthome.uicomponent

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import com.smarthome.R
import com.smarthome.activity.HomeActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class HomeUI : AnkoComponent<HomeActivity> {
    lateinit var recyclerView: RecyclerView
    lateinit var loadLty: RelativeLayout

    override fun createView(ui: AnkoContext<HomeActivity>) =  with(ui)  {
        relativeLayout {
            recyclerView = recyclerView {
                clipToPadding = false
                topPadding = dip(5)
                id = View.generateViewId()
            }.lparams{
                width = matchParent
                height = matchParent
            }
            loadLty = relativeLayout{
                backgroundColor = Color.WHITE
                progressBar {
                }.lparams{
                    width = wrapContent
                    height = wrapContent
                    gravity = Gravity.CENTER
                }
            }.lparams{
                width = matchParent
                height = matchParent
            }
        }
    }

}