package com.chatter.furrki.chatter.Models

import com.parse.ParseClassName
import com.parse.ParseUser
@ParseClassName("_User")
public class User: ParseUser() {
    init {

    }
    fun current(): ParseUser? {
        return getCurrentUser()
    }
    fun Pu(pu: ParseUser){
        this.let { pu }
    }
    fun getQuery(): Unit{
        return this.getQuery()
    }
}