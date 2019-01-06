package com.chatter.furrki.chatter.Models

import android.util.Log
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

@ParseClassName("Room")
class Room: ParseObject() {

    companion object {
        const val stepsize = 10

    }
    var messages:ArrayList<Message> = ArrayList()

    fun getMembers(): ArrayList<ParseUser> {
        return get("Members") as ArrayList<ParseUser>
    }
    var isLoading = false
    fun loadNext(finished: (newMessages: List<Message>) -> Unit){
        if (!isLoading) {
            val query = ParseQuery.getQuery<Message>("Message")
            query.whereEqualTo("Room", this)
            query.addDescendingOrder("createdAt")
            query.skip = messages.size
            query.limit = stepsize
            isLoading = true
            query.findInBackground { messagess, e ->
                if (e == null) {
                    for(msg: Message in messagess){
                        this@Room.messages.add(0, msg)
                    }
                    finished(messages)
                } else {
                    Log.d("room", "Error: " + e.message)
                }
                isLoading = false
            }
        }


    }

}