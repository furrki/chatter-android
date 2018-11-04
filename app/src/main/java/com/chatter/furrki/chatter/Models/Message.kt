package com.chatter.furrki.chatter.Models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.parse.*

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
    var hasImage: Boolean = false
        get() = has("Image")

    var img: ParseFile
        get() = get("Image") as ParseFile
        set(value){
            add("Image", value)
        }

    var imgBitmapTmp: Bitmap? = null
    var imgBitmap: Bitmap? = null
        get(){
        if(imgBitmapTmp == null){
            val data = img.data
            val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
            imgBitmapTmp = bmp
            return bmp
        }
        return imgBitmapTmp!!
    }

}