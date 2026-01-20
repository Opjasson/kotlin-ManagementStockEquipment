package com.example.nasibakarjoss18_application.Helper

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ConvertDateTime {

    fun formatTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        return sdf.format(date)
    }
}