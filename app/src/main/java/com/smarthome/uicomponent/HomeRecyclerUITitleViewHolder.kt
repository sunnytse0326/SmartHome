package com.smarthome.uicomponent

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Switch
import android.widget.TextView

class HomeRecyclerUITitleViewHolder(mainUI: HomeRecyclerViewTitleUI, itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView

    init {
        title = mainUI.title
    }

}