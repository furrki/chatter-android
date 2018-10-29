package com.chatter.furrki.chatter

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Message")

class Message: ParseObject() {
    var text: String = ""
        get(){
            return get("Text") as String
        }
    var owner: ParseUser = ParseUser()
        get(){
            val owner = getParseUser("Owner")
            owner!!.fetchIfNeeded()
            return owner
        }


}