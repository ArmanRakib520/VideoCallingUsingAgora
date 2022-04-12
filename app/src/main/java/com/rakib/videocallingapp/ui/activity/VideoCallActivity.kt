package com.rakib.videocallingapp.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rakib.videocallingapp.R
import com.rakib.videocallingapp.util.LogWriter
import com.rakib.videocallingapp.util.Utility
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas

class VideoCallActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_RECORD_AUDIO = 22
        const val PERMISSION_CAMERA = PERMISSION_RECORD_AUDIO + 1
    }

    private var mRtcEngine: RtcEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        if (checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,
                PERMISSION_RECORD_AUDIO
            ) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_CAMERA)
        ) {
            initializeAndJoinChannel()
        }

        endCall()
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                requestCode
            )
            return false
        }
        return true
    }

    private fun endCall(){
        val callEnd = findViewById<ImageView>(R.id.callEnd)
        callEnd.setOnClickListener {
            finish()
        }
    }

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                setupRemoteVideo(uid)
            }
        }
    }

    private fun initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, Utility.APP_ID, mRtcEventHandler)
        } catch (e: Exception) {
            LogWriter.logWriter.writToLog("AAA", "Fail")
        }
        mRtcEngine!!.enableVideo()

        val localContainer = findViewById<FrameLayout>(R.id.local_video_view_container)
        val localFrame = RtcEngine.CreateRendererView(baseContext)

        localContainer.addView(localFrame)

        mRtcEngine!!.setupLocalVideo(VideoCanvas(localFrame, VideoCanvas.RENDER_MODE_FIT, 0))
        LogWriter.logWriter.writToLog("AAA", "Join")

        mRtcEngine!!.joinChannel(Utility.ACCESS_TOKEN, Utility.CHANNEL_NAME, "", 0)
        LogWriter.logWriter.writToLog("AAA", "Joined")
        LogWriter.logWriter.writToLog("AAA", "Joined ${Utility.ACCESS_TOKEN}")
        LogWriter.logWriter.writToLog("AAA", "Joined ${Utility.CHANNEL_NAME}")
    }

    private fun setupRemoteVideo(uid: Int) {
        val remoteContainer = findViewById<FrameLayout>(R.id.remote_video_view_container)

        val remoteFrame = RtcEngine.CreateRendererView(baseContext)
        remoteFrame.setZOrderMediaOverlay(true)
        remoteContainer.addView(remoteFrame)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(remoteFrame, VideoCanvas.RENDER_MODE_FIT, uid))
    }

    override fun onDestroy() {
        super.onDestroy()
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
    }
}