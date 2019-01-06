package com.chatter.furrki.chatter.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chatter.furrki.chatter.Adapters.ChatListViewAdapter
import com.chatter.furrki.chatter.Models.Message
import com.chatter.furrki.chatter.R
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import android.content.Intent
import android.R.attr.data
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.appcompat.R.id.scrollView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.chatter.furrki.chatter.Models.Room
import com.parse.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlinx.android.synthetic.main.activity_chat.*
import java.security.AccessController.getContext
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING


class ChatActivity : AppCompatActivity() {
    var chatId = ""
    lateinit var room: Room
    lateinit var chatList: ListView
    lateinit var msgEt: EditText
    lateinit var galleryButton: ImageButton
    private var listViewAdapter: ChatListViewAdapter? = null
    var imgFile: ParseFile? = null
    val PICK_IMAGE = 1
    var isSending = false

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    val mainBitmap = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
                    var bytes = ByteArrayOutputStream()
                    mainBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, bytes)
                    var imageBytes = bytes.toByteArray()
                    imgFile = ParseFile("resume.jpg", imageBytes)
                    galleryButton.background =  ColorDrawable(Color.parseColor("#AAAAAAAA"))
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
        galleryButton = findViewById(R.id.galleryBtn)

        chatId = intent.getStringExtra("chatId")

        val query = ParseQuery.getQuery<ParseObject>("Room")
        this.room = query.get(chatId) as Room

        supportActionBar!!.title = this.room.opUser.username


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
        2

        chatList.setOnScrollListener(object : AbsListView.OnScrollListener {
            private var firstVisibleItem = 0
            override fun onScrollStateChanged(view:AbsListView, scrollState:Int) {
                Log.d("ListView", firstVisibleItem.toString())
                Log.d("State", scrollState.toString())
                if( this.firstVisibleItem <= 1){
                    room.loadNext {
                        listViewAdapter!!.notifyDataSetChanged()
                    }
                }
            }
            override fun onScroll(view:AbsListView, firstVisibleItem:Int, visibleItemCount:Int, totalItemCount:Int) {
                this.firstVisibleItem = firstVisibleItem
            }

        })
    }

    private fun listIsAtTop(): Boolean {
        return if (chatList.childCount == 0) true else chatList.getChildAt(0).top == 0
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
        if(!isSending) {
            if (msgEt.text.toString().trim() != "" || imgFile != null) {

                isSending = true
                val msg = Message()
                msg.put("Room", this.room)
                msg.put("Text", msgEt.text.toString())
                msg.put("Owner", ParseUser.getCurrentUser())
                if (imgFile != null) {
                    msg.put("Image", imgFile!!)
                    imgFile!!.save()
                }
                msg.saveInBackground(SaveCallback { e ->
                    if (e == null) {
                        this@ChatActivity.msgEt.text.clear()
                        this@ChatActivity.imgFile = null
                    } else {
                        e.printStackTrace()
                    }
                    isSending = false
                })
            }
        }
    }
}
