package com.chatter.furrki.chatter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import com.parse.ParseObject
import com.parse.GetCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.FindCallback
import com.parse.FunctionCallback
import com.parse.ParseCloud
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling


class ChatActivity : AppCompatActivity() {
    var chatId = ""
    lateinit var room : ParseObject
    var msgs = ArrayList<Message>()
    lateinit var chatList : ListView
    lateinit var msgEt : EditText
    private var listViewAdapter: ChatListViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatList = findViewById(R.id.chatList)
        msgEt = findViewById(R.id.chatText)
        val sendBtn = findViewById<ImageButton>(R.id.sendBtn)

        chatId = intent.getStringExtra("chatId")

        val query = ParseQuery.getQuery<ParseObject>("Room")
        this.room = query.get(chatId)

        listViewAdapter = ChatListViewAdapter(this, msgs)
        chatList.adapter = listViewAdapter

        retrieveMsgs()
        listenToMessages()

        sendBtn.setOnClickListener {
            sendMessage()
        }
    }
    fun retrieveMsgs(){
        val query = ParseQuery.getQuery<Message>("Message")
        query.whereEqualTo("Room", this.room)
        query.findInBackground { messages, e ->
            if (e == null) {
                msgs = messages as ArrayList<Message>
                for(msg: Message in msgs){
                    listViewAdapter!!.add(msg)
                }
                scrollMyListViewToBottom()


            } else {
                Log.d("score", "Error: " + e.message)
            }
        }
    }
    fun listenToMessages(){
        val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()

        val parseQuery = ParseQuery.getQuery<Message>("Message")
        parseQuery.whereEqualTo("Room", this.room)
        val subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { query, msg ->
            listViewAdapter!!.add(msg)
            listViewAdapter!!.notifyDataSetChanged()
            scrollMyListViewToBottom()
        }
    }

    private fun scrollMyListViewToBottom() {
        chatList.post {
            // Select the last row so it will scroll into view...
            chatList.setSelection(listViewAdapter!!.count - 1)
        }
    }

    private fun sendMessage(){
        val params = HashMap<String, Any>()
        params["room"] = this.room.objectId
        params["text"] = msgEt.text.toString()
        ParseCloud.callFunctionInBackground("sendMsg", params, FunctionCallback<String> { ratings, e ->
            if (e == null) {
                this@ChatActivity.msgEt.text.clear()
            }
        })
    }
}
