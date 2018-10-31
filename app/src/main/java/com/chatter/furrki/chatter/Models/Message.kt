package com.chatter.furrki.chatter.Models

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Message")
class Message: ParseObject() {


    fun owner(): ParseUser {
        val owner = getParseUser("Owner")
        owner!!.fetchIfNeeded()
        return owner
    }
    fun text(): String {

        return get("Text") as String
    }

}