package com.chatter.furrki.chatter.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.parse.*
import android.view.*
import com.chatter.furrki.chatter.Adapters.RoomListViewAdapter
import com.chatter.furrki.chatter.Models.Room
import com.chatter.furrki.chatter.R
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling


class HomeActivity : AppCompatActivity() {
    private var listViewAdapter: RoomListViewAdapter? = null
    private var persons: ArrayList<Room>? = null
    lateinit var roomList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        initialize()
        loadRooms()
        roomList.onItemClickListener = AdapterView.OnItemClickListener { l, v, pos, id ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatId", listViewAdapter!!.getItem(pos)!!.objectId)
            startActivity(intent)
        }
    }

    private fun initialize() {
        persons = ArrayList()
        roomList = findViewById(R.id.roomList)
        listViewAdapter = RoomListViewAdapter(this, persons!!)
        roomList.adapter = listViewAdapter


        val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()

        val parseQuery = ParseQuery.getQuery<Room>("Room")
        val subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { query, room ->

            addRoom(room)
        }
    }
    fun loadRooms(){
        val query = ParseQuery.getQuery<Room>("Room")
        query.include("User")
        Log.d("aa", ParseUser.getCurrentUser().username)
        query.whereEqualTo("Members", ParseUser.getCurrentUser())
        query.findInBackground { rooms, e ->
            if (e == null) {
                for (room: Room in rooms) {

                    addRoom(room)
                }
            } else {
                Log.d("err", "Error: " + e.message)
            }
        }
    }

    fun addRoom(room: Room){
        persons!!.add(room)
        runOnUiThread {
            listViewAdapter!!.notifyDataSetChanged()
        }

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_menu_add_chat -> {
                val intent = Intent(this, AddChat::class.java)
                startActivity(intent)
                return true
            }
            R.id.main_menu_logout -> {
                ParseUser.logOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
