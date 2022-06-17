package com.example.hinvipskotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.media.MediaPlayer
import android.os.Vibrator
import android.widget.Button

@Suppress("DEPRECATION")
class InfoActivity : AppCompatActivity() {
    var mediaPlayerDoor: MediaPlayer? = null
    var vibe: Vibrator? = null
    var mediaPlayerCorridor: MediaPlayer? = null
    var mediaPlayerElevator: MediaPlayer? = null
    var mediaPlayerStairs: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val buttonDoor = findViewById (R.id.button_Door) as Button
        val buttonWall = findViewById (R.id.button_Wall) as Button
        val buttonCorridor = findViewById (R.id.button_Corridor) as Button
        val buttonElevator = findViewById (R.id.button_Elevator) as Button
        val buttonStairs = findViewById (R.id.button_Stairs) as Button

        mediaPlayerDoor = MediaPlayer.create(this, R.raw.door)
        vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayerCorridor = MediaPlayer.create(this, R.raw.corridor)
        mediaPlayerElevator = MediaPlayer.create(this, R.raw.elevator)
        mediaPlayerStairs = MediaPlayer.create(this, R.raw.stairs)

        buttonDoor.setOnTouchListener { _, event ->
            handleTouchDoor(event)
            true
        }

        buttonWall.setOnTouchListener{_, event ->
            handleTouchWalls(event)
            true
        }

        buttonCorridor.setOnTouchListener { _, event ->
            handleTouchCorridor(event)
            true
        }

        buttonElevator.setOnTouchListener { _, event ->
            handleTouchElevator(event)
            true
        }

        buttonStairs.setOnTouchListener { _, event ->
            handleTouchStairs(event)
            true
        }

    }
    private fun handleTouchDoor(event:MotionEvent){
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                mediaPlayerDoor?.start()
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP->{
                mediaPlayerDoor?.pause()
                mediaPlayerDoor?.seekTo(0)
            }
        }
    }

    private fun handleTouchWalls(event:MotionEvent){
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                vibe?.vibrate(5000000)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                vibe?.cancel()
            }
        }
    }


    private fun handleTouchCorridor(event:MotionEvent){
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                mediaPlayerCorridor?.start()
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP->{
                mediaPlayerCorridor?.pause()
                mediaPlayerCorridor?.seekTo(0)
            }
        }
    }

    private fun handleTouchElevator(event:MotionEvent){
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                mediaPlayerElevator?.start()
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP->{
                mediaPlayerElevator?.pause()
                mediaPlayerElevator?.seekTo(0)
            }
        }
    }

    private fun handleTouchStairs(event:MotionEvent){
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                mediaPlayerStairs?.start()
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP->{
                mediaPlayerStairs?.pause()
                mediaPlayerStairs?.seekTo(0)
            }
        }
    }
}