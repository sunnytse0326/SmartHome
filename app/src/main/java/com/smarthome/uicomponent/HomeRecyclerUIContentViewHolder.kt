package com.smarthome.uicomponent

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Switch
import android.widget.TextView

class HomeRecyclerUIContentViewHolder(mainUI: HomeRecyclerViewContentUI, itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = mainUI.title
    var switch: Switch = mainUI.switch
}