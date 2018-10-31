package com.chatter.furrki.chatter.Models

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Room")
class Room: ParseObject() {
    companion object {

    }
        fun getMembers(): ArrayList<ParseUser> {
            return get("Members") as ArrayList<ParseUser>
        }
}