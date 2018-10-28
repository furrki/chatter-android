package com.chatter.furrki.chatter

import com.parse.ParseClassName
import com.parse.ParseUser
@ParseClassName("User")
class User: ParseUser() {
    init {

    }
    fun current(): ParseUser? {
        return getCurrentUser()
    }
    fun Pu(pu: ParseUser){
        this.let { pu }
    }

}