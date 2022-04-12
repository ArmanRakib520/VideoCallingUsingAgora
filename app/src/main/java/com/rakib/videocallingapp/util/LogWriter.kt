package com.rakib.videocallingapp.util

import android.util.Log

class LogWriter {
    companion object{
        val logWriter = LogWriter()
        fun get() = logWriter
    }
    private val isDebugMode = true

    fun writToLog(tag : String, message : String){
        if (!isDebugMode) return
        Log.d(tag, message)
    }
}