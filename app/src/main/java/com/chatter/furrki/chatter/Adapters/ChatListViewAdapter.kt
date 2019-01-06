package com.chatter.furrki.chatter.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatter.furrki.chatter.Models.Message
import com.chatter.furrki.chatter.R

import com.parse.ParseUser
import kotlinx.android.synthetic.main.chat_o_item.view.*

import java.util.ArrayList
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.*
import com.chatter.furrki.chatter.Models.Room
import com.parse.GetDataCallback
import com.parse.ParseException
import com.parse.ParseFile





class ChatListViewAdapter(context: Context, private val room: Room) : ArrayAdapter<Message>(context, 0) {

    private val inflater: LayoutInflater
    private var holder: ViewHolder? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return this.room.messages.size
    }

    override fun getItem(position: Int): Message? {

        return this.room.messages[position]
    }

    override fun getItemId(position: Int): Long {
        return this.room.messages[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        /*if(position == 0){
            (parent as ListView).post {
                // Select the last row so it will scroll into view...
                (parent).setSelection(3)
            }
            room.loadNext {
                this.notifyDataSetChanged()

            }
        }*/
        var convertView = convertView

        val msg = this.room.messages[position]

        if (msg.owner().objectId == ParseUser.getCurrentUser().objectId) {

            convertView = inflater.inflate(R.layout.chat_m_item, null)
        } else {
            convertView = inflater.inflate(R.layout.chat_o_item, null)

        }

        holder = ViewHolder()
        holder!!.img = convertView.findViewById<View>(R.id.atimg) as ImageView
        convertView.tag = holder

        val content = msg.text()

        if(content != ""){
            holder!!.content = convertView.findViewById<View>(R.id.chatTw) as TextView
            holder!!.content!!.text = content
        } else {

            holder!!.content = convertView.findViewById<View>(R.id.chatTw) as TextView
            (holder!!.content!!.parent as ViewGroup).removeView(holder!!.content)
        }
        if(msg.hasImage){

            holder!!.img!!.setImageBitmap(msg.imgBitmap)
        }
        else
            holder!!.img!!.layoutParams = LinearLayout.LayoutParams(0, 0)

        return convertView!!
    }

    //View Holder Pattern for better performance
    private class ViewHolder {
        internal var content: TextView? = null
        internal var img: ImageView? = null
    }

    private fun loadImages(thumbnail: ParseFile?, img: ImageView) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(GetDataCallback { data, e ->
                if (e == null) {
                    val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
                    img.setImageBitmap(bmp)
                } else {
                }
            })
        } else {
            img.setImageResource(R.mipmap.ic_launcher)
        }
    }
}