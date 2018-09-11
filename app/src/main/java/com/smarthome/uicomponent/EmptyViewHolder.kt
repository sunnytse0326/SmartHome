package com.smarthome.uicomponent

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout


class EmptyViewHolder : RecyclerView.ViewHolder {
    var content: RelativeLayout

    constructor(mainUI: EmptyViewUI, itemView: View, mContent: View?) : super(itemView) {
        if (mContent?.parent != null) {
            (mContent?.parent as ViewGroup).removeAllViewsInLayout()
        }
        content = mainUI.relativeLayout
        content.removeAllViewsInLayout()
        content.addView(mContent)
    }
}