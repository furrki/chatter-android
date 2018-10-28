package com.chatter.furrki.chatter

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.*
import com.parse.*
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.view.*
import android.widget.TextView
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling


class HomeActivity : AppCompatActivity() {

    private var listViewAdapter: RoomListViewAdapter? = null
    private var persons: ArrayList<ParseUser>? = null
    lateinit var roomList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        initialize()
        loadRooms()

    }

    private fun initialize() {
        persons = ArrayList()
        roomList = findViewById(R.id.roomList)
        listViewAdapter = RoomListViewAdapter(this, persons!!)
        roomList.adapter = listViewAdapter


        val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()

        var parseQuery = ParseQuery.getQuery<ParseObject>("Room")
        var subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { query, room ->
            val members = room.get("Members") as ArrayList<ParseUser>
            var other = ParseUser.getCurrentUser()
            if(members.get(0).objectId == other.objectId){
                other = members[1]
            } else {
                other = members[0]
            }
            addRoom(other)
        }
    }
    fun loadRooms(){
        val query = ParseQuery.getQuery<ParseObject>("Room")
        query.include("User")
        Log.d("aa", ParseUser.getCurrentUser().username)
        query.whereEqualTo("Members", ParseUser.getCurrentUser())
        query.findInBackground { rooms, e ->
            if (e == null) {
                for (room: ParseObject in rooms) {
                    val members = room.get("Members") as ArrayList<ParseUser>
                    var other = ParseUser.getCurrentUser()
                    if(members.get(0).objectId == other.objectId){
                        other = members[1]
                    } else {
                        other = members[0]
                    }
                    addRoom(other)
                }
            } else {
                Log.d("err", "Error: " + e.message)
            }
        } 
    }

    fun addRoom(other: ParseUser){
        other.fetchIfNeeded()
        persons!!.add(other)
        Log.d("Other", other.username)
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
            R.id.main_menu_logout -> {
                ParseUser.logOut()
                val intent = Intent(this,MainActivity::class.java)
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
