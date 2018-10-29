package com.chatter.furrki.chatter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.parse.ParseObject
import com.parse.GetCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.FindCallback





class ChatActivity : AppCompatActivity() {
    var chatId = ""
    lateinit var room : ParseObject
    var msgs = ArrayList<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatId = intent.getStringExtra("chatId")

        val query = ParseQuery.getQuery<ParseObject>("Room")
        this.room = query.get(chatId)

        retrieveMsgs()


    }
    fun retrieveMsgs(){
        val query = ParseQuery.getQuery<Message>("Message")
        query.whereEqualTo("Room", this.room)
        query.findInBackground { messages, e ->
            if (e == null) {
                msgs = messages as ArrayList<Message>
            } else {
                Log.d("score", "Error: " + e.message)
            }
        }
    }
}
