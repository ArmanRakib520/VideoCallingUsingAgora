package com.rakib.videocallingapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rakib.videocallingapp.R

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startVideoCall()
        startChat()

    }

    private fun startVideoCall(){
        val tvVideoCall = findViewById<TextView>(R.id.tvVideoCall)
        tvVideoCall.setOnClickListener {
            val videoCallIntent = Intent(applicationContext, VideoCallActivity::class.java)
            startActivity(videoCallIntent)
        }
    }

    private fun startChat(){
        val tvChat = findViewById<TextView>(R.id.tvChat)
        tvChat.setOnClickListener {
            val chatIntent = Intent(applicationContext, ChatActivity::class.java)
            startActivity(chatIntent)
        }
    }

}