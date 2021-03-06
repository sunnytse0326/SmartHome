package com.smarthome.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.smarthome.R
import com.smarthome.model.Room
import com.smarthome.model.Rooms
import com.smarthome.repository.RoomRepository
import com.smarthome.uicomponent.*
import org.jetbrains.anko.AnkoContext

class HomeRecyclerViewAdapter constructor(mContext: Context, mListener: OnClickListener, mRooms: Rooms) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var headerView: View? = null
    private var footerView: View? = null

    private val context = mContext
    var listener: OnClickListener = mListener
    var rooms: Rooms = mRooms

    private enum class Type {
        Header, Title, Content, Footer
    }

    fun setHeaderView(view: View) {
        headerView = view
    }

    fun setRoomData(mRooms: Rooms?) {
        rooms = mRooms ?: Rooms(Room(mutableMapOf()), Room(mutableMapOf()), Room(mutableMapOf()))
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && headerView != null) {
            Type.Header.ordinal
        } else if (position == itemCount - 1 && footerView != null) {
            Type.Footer.ordinal
        } else {
            if (position == 0 || position == rooms?.bedRoom?.fixture?.size + 1 || position == (rooms?.livingRoom?.fixture?.size + rooms?.bedRoom?.fixture?.size + 2)) {
                Type.Title.ordinal
            } else {
                Type.Content.ordinal
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Type.Header.ordinal) {
            val ui = EmptyViewUI()
            EmptyViewHolder(ui, ui.createView(AnkoContext.create(parent.context, parent)), headerView)
        } else if (viewType == Type.Footer.ordinal) {
            val ui = EmptyViewUI()
            EmptyViewHolder(ui, ui.createView(AnkoContext.create(parent.context, parent)), footerView)
        } else if (viewType == Type.Title.ordinal) {
            val ui = HomeRecyclerViewTitleUI()
            HomeRecyclerUITitleViewHolder(ui, ui.createView(AnkoContext.create(parent.context, parent)))
        } else {
            val ui = HomeRecyclerViewContentUI()
            HomeRecyclerUIContentViewHolder(ui, ui.createView(AnkoContext.create(parent.context, parent)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeRecyclerUITitleViewHolder) {
            if (position == 0) {
                holder.title.text = context.resources.getString(R.string.bedroom)
            } else if (position == rooms?.bedRoom?.fixture?.size + 1) {
                holder.title.text = context.resources.getString(R.string.living_room)
            } else if (position == rooms?.bedRoom?.fixture?.size + rooms?.livingRoom?.fixture?.size + 2) {
                holder.title.text = context.resources.getString(R.string.kitchen)
            }
        } else if (holder is HomeRecyclerUIContentViewHolder) {
            holder.switch.setOnCheckedChangeListener(null)

            if (position > 0 && position < rooms?.bedRoom?.fixture?.size + 1) {
                val key = rooms?.bedRoom?.fixture.keys?.elementAt(position - 1)
                holder.title.text = key

                holder.switch.isChecked = rooms?.bedRoom?.fixture.get(key)?:false

                holder.switch.setOnCheckedChangeListener { _, isChecked ->
                    rooms?.bedRoom?.fixture?.put(key, isChecked)
                    listener?.onSwitchChanged(isChecked, "/${RoomRepository.Companion.RoomType.BEDROOM.url}/${key.toLowerCase()}/${if (isChecked) "on" else "off"}")
                }
            } else if (position > rooms?.bedRoom?.fixture?.size + 1 && position < rooms?.bedRoom?.fixture?.size + rooms?.livingRoom?.fixture?.size + 2) {
                val key = rooms?.livingRoom?.fixture.keys?.elementAt((position - rooms?.bedRoom?.fixture?.size - 2))
                holder.title.text = key

                holder.switch.isChecked = rooms?.livingRoom?.fixture.get(key)?:false

                holder.switch.setOnCheckedChangeListener { _, isChecked ->
                    rooms?.livingRoom?.fixture?.put(key, isChecked)
                    listener?.onSwitchChanged(isChecked, "/${RoomRepository.Companion.RoomType.LIVING_ROOM.url}/${key.toLowerCase()}/${if (isChecked) "on" else "off"}")
                }
            } else if (position > rooms?.bedRoom?.fixture?.size + rooms?.livingRoom?.fixture?.size + 2) {
                val key = rooms?.kitchen?.fixture.keys?.elementAt((position - rooms?.bedRoom?.fixture?.size - rooms?.livingRoom?.fixture?.size - 3))
                holder.title.text = key

                holder.switch.isChecked = rooms?.kitchen?.fixture.get(key)?:false

                holder.switch.setOnCheckedChangeListener { _, isChecked ->
                    rooms?.kitchen?.fixture?.put(key, isChecked)
                    listener?.onSwitchChanged(isChecked, "/${RoomRepository.Companion.RoomType.KITCHEN.url}/${key.toLowerCase()}/${if (isChecked) "on" else "off"}")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return rooms?.bedRoom?.fixture?.size + rooms?.livingRoom?.fixture?.size + rooms?.kitchen?.fixture?.size + 3 + (if (headerView != null) 1 else 0) + (if (footerView != null) 1 else 0)
    }

    interface OnClickListener {
        fun onSwitchChanged(checked: Boolean, url: String)
    }

}