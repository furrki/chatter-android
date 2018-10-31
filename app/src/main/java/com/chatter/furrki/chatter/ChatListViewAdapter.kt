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

class ChatListViewAdapter(context: Context, private val msgs: ArrayList<Message>) : ArrayAdapter<Message>(context, 0, msgs) {

    private val inflater: LayoutInflater
    private var holder: ViewHolder? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return msgs.size
    }

    override fun getItem(position: Int): Message? {
        return msgs[position]
    }

    override fun getItemId(position: Int): Long {
        return msgs[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val msg = msgs[position]

        if (msg.owner().objectId == ParseUser.getCurrentUser().objectId) {

            convertView = inflater.inflate(R.layout.chat_m_item, null)
        } else {
            convertView = inflater.inflate(R.layout.chat_o_item, null)

        }

        holder = ViewHolder()
        holder!!.content = convertView.findViewById<View>(R.id.chatTw) as TextView
        convertView.tag = holder

        val content = msg.text()

        if (msg.owner().objectId == ParseUser.getCurrentUser().objectId) {
            holder!!.content!!.text = content
        } else {
            holder!!.content!!.text = content
        }
        return convertView!!
    }

    //View Holder Pattern for better performance
    private class ViewHolder {
        internal var content: TextView? = null
    }
}