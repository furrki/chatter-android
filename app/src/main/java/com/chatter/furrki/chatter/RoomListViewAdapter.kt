package com.chatter.furrki.chatter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.parse.ParseUser

import java.util.ArrayList

class RoomListViewAdapter(context: Context, private val rooms: ArrayList<Room>) : ArrayAdapter<Room>(context, 0, rooms) {

    private val inflater: LayoutInflater
    private var holder: ViewHolder? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return rooms.size
    }

    override fun getItem(position: Int): Room? {
        return rooms[position]
    }

    override fun getItemId(position: Int): Long {
        return rooms[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.room_list_item, null)

            holder = ViewHolder()
            holder!!.userimage = convertView!!.findViewById<View>(R.id.user_image) as ImageView
            holder!!.username = convertView.findViewById<View>(R.id.user_name) as TextView
            convertView.tag = holder

        } else {
            //Get viewholder we already created
            holder = convertView.tag as ViewHolder
        }

        val room = rooms[position]
        if (room != null) {
            //holder.userimage.setImageResource();
            val members = room.getMembers()
            var other = ParseUser.getCurrentUser()

            if (members[0].objectId == other.objectId) {
                other = members[1]
            } else {
                other = members[0]
            }
            other.fetchIfNeeded()


            holder!!.username!!.setText(other.getUsername())

        }
        return convertView
    }

    //View Holder Pattern for better performance
    private class ViewHolder {
        internal var username: TextView? = null
        internal var userimage: ImageView? = null

    }
}