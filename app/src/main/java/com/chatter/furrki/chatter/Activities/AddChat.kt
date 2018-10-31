package com.chatter.furrki.chatter.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import com.parse.ParseUser
import android.widget.Toast
import com.chatter.furrki.chatter.Adapters.UsersListViewAdapter
import com.chatter.furrki.chatter.Models.User
import com.chatter.furrki.chatter.R
import com.parse.FunctionCallback
import com.parse.ParseCloud





class AddChat : AppCompatActivity(), SearchView.OnQueryTextListener {



    lateinit var list: ListView
    lateinit var adapter: UsersListViewAdapter
    lateinit var editsearch: SearchView
    var animalNameList = ArrayList<String>()
    var arraylist = ArrayList<User>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chat)

        // Generate sample data

        // Locate the ListView in listview_main.xml
        list = findViewById<ListView>(R.id.search_list)


        // Pass results to ListViewAdapter Class
        val query =  ParseUser.getQuery()
        query.findInBackground { users, e ->
            if (e == null) {

                for (puser: ParseUser in users) {
                    val user = puser as User
                    arraylist.add(user)
                    animalNameList.add(user.username)

                    adapter = UsersListViewAdapter(this, arraylist)
                    list.adapter = adapter
                }
            } else {
                // Something went wrong.
            }
        }
        editsearch = findViewById<View>(R.id.search) as SearchView
        editsearch.setOnQueryTextListener(this)
        list.onItemClickListener = AdapterView.OnItemClickListener { l, v, pos, id ->
            Log.d("User", adapter.getItem(pos).username)


            val params = HashMap<String, Any>()
            params["who"] = adapter.getItem(pos).objectId

            ParseCloud.callFunctionInBackground("createRoom", params, FunctionCallback<String> { ret, e ->
                if (e == null) {

                    Toast.makeText(applicationContext, ret, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Error " + e.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {

        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter.filter(newText)
        return false
    }
}
