package com.smarthome.uicomponent

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.smarthome.R
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class HomeRecyclerViewContentUI : AnkoComponent<ViewGroup> {
    lateinit var title: TextView
    lateinit var switch: Switch

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            padding = dip(5)
            lparams(matchParent, wrapContent)
            title = textView {
                textSize = 18f
                textColor = R.color.colorBlack
            }.lparams {
                width = 0
                height = wrapContent
                weight = 1.0F
            }
            switch = switch {
            }.lparams {
                width = wrapContent
                height = wrapContent
            }
        }
    }
}