package com.chatter.furrki.chatter

import android.content.Context
import android.widget.TableRow
import android.widget.TextView

class RoomRow(context: Context?) : TableRow(context) {
    var username: String = ""
        get() = this.toString()

    init {
        // Set new table row layout parameters.
        val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
        this.layoutParams = layoutParams

        // Add a TextView in the first column.
        val textView = TextView(context)
        this.addView(textView, 0)
    }
}