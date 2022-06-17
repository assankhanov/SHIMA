package com.example.hinvipskotlin

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import android.content.Intent


import android.widget.RelativeLayout

import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_room_map.*
import android.view.MotionEvent
import android.view.View.OnTouchListener
//import android.R
import android.view.View
import android.widget.LinearLayout


/**
 * MapActivity
 *
 * Activity that display the map. Will be launched by the XMLActivity.
 *
 * @constructor create the map to be displayed
 */
class RoomMapActivity : AppCompatActivity() {


    /**on Create
     * Basic operations when the Activity is created.
     *
     * Initialise the activity and settle it's appearance
     * Create the relativeLayout that will handle the CanvassRoom on which the map will be drawed upon
     *
     * @param Bundle? State of the activity when it was last called
     * @since 0.0.1
     */


    private lateinit var background: Canvass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_room_map)
        //for drawing the map
        val layout1 = layout2 as LinearLayout
        val canvass = CanvassRoom(this, XMLActivity())
        layout1.addView(canvass)
        canvass.setOnTouchListener(canvass)
        canvass.isFocusable = true



        canvass.setOnTouchListener(canvass)





    }


}