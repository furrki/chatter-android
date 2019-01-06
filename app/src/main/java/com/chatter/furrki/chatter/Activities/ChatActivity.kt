package com.chatter.furrki.chatter.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import com.chatter.furrki.chatter.Adapters.ChatListViewAdapter
import com.chatter.furrki.chatter.Models.Message
import com.chatter.furrki.chatter.R
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import android.content.Intent
import android.R.attr.data
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.view.View
import com.chatter.furrki.chatter.Models.Room
import com.parse.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import android.widget.AbsListView




class ChatActivity : AppCompatActivity() {
    var chatId = ""
    lateinit var room: Room
    lateinit var chatList: ListView
    lateinit var msgEt: EditText
    private var listViewAdapter: ChatListViewAdapter? = null
    var imgFile: ParseFile? = null
    val PICK_IMAGE = 1

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    val mainBitmap = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
                    var bytes = ByteArrayOutputStream()
                    mainBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, bytes)
                    var imageBytes = bytes.toByteArray()
                    imgFile = ParseFile("resume.jpg", imageBytes)
                    //val thb = Bitmap.createScaledBitmap(mainBitmap!!, 160, 169, true)

                    // bytes = ByteArrayOutputStream()
                    // thb.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
                    //  thbBytes = bytes.toByteArray()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatList = findViewById(R.id.chatList)
        msgEt = findViewById(R.id.chatText)
        val sendBtn = findViewById<ImageButton>(R.id.sendBtn)
        val galleryBtn = findViewById<ImageButton>(R.id.galleryBtn)

        chatId = intent.getStringExtra("chatId")

        val query = ParseQuery.getQuery<ParseObject>("Room")
        this.room = query.get(chatId) as Room

        listViewAdapter = ChatListViewAdapter(this, room)
        chatList.adapter = listViewAdapter

        this.room.loadNext {
            scrollMyListViewToBottom()
        }
        listenToMessages()

        sendBtn.setOnClickListener {
            sendMessage()
        }
        galleryBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }

    }
    fun loadMore() {
        room.loadNext {
            listViewAdapter!!.notifyDataSetChanged()
        }
    }
    fun listenToMessages(){
        val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()

        val parseQuery = ParseQuery.getQuery<Message>("Message")
        parseQuery.whereEqualTo("Room", this.room)
        val subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { query, msg ->

            this@ChatActivity.runOnUiThread {
                this.room.messages.add(msg)
                listViewAdapter!!.notifyDataSetChanged()
                scrollMyListViewToBottom()
            }
        }
    }

    private fun scrollMyListViewToBottom() {
        chatList.post {
            // Select the last row so it will scroll into view...
            chatList.setSelection(listViewAdapter!!.count - 1)
        }
    }

    private fun sendMessage(){
        val msg = Message()
        msg.put("Room", this.room)
        msg.put("Text",  msgEt.text.toString())
        msg.put("Owner",  ParseUser.getCurrentUser())
        if(imgFile != null) {
            msg.put("Image", imgFile!!)
            imgFile!!.save()
        }
        msg.saveInBackground( SaveCallback { e ->
                if (e == null) {
                    this@ChatActivity.msgEt.text.clear()
                    this@ChatActivity.imgFile = null
                } else {
                    e.printStackTrace()
                }
        })



    }
}
