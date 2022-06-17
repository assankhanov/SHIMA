package com.example.hinvipskotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import android.content.Intent
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout


import android.widget.RelativeLayout

import kotlinx.android.synthetic.main.activity_map.*
import kotlin.math.abs
import android.view.MotionEvent
//import android.R




/**
 * MapActivity
 *
 * Activity that display the map. Will be launched by the XMLActivity.
 *
 * @constructor create the map to be displayed
 */
class MapActivity() : AppCompatActivity() {


    var xmlparser: XMLActivity = XMLActivity()

    /**on Create
     * Basic operations when the Activity is created.
     *
     * Initialise the activity and settle it's appearance
     * Create the relativeLayout that will handle the Canvass on which the map will be drawed upon
     *
     * @param Bundle? State of the activity when it was last called
     * @since 0.0.1
     */


    private lateinit var background: Canvass

    override fun onCreate(savedInstanceState: Bundle?) {




        //val aymax = 100
        val aymax = abs(xmlparser.ymax)
        val axmax = abs(xmlparser.xmax)
        val aymin = abs(xmlparser.ymin)
        val axmin = abs(xmlparser.xmin)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        //val resizeVal = (widthScreen/(axmax*1.1)).toFloat()


        val myresizeVal = if(aymax<=axmax){
            (width/(axmax*1.1)).toFloat()
        }
        else {
            (height/(aymax*1.6)).toFloat()
        }

        Log.e("HI MAP ACTIVITY", String.format("HI MAP ACTIVITY %f  %f  %f MIN   %f %f",aymax,axmax,myresizeVal,aymin,axmin))


        //val axmax = 20
        //val resizeVal = 25

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)

        // Gets linearlayout
        val layout: LinearLayout = layout1
        // Gets the layout params that will allow you to resize the layout
        val params: ViewGroup.LayoutParams = layout.layoutParams
        params.width = (myresizeVal*(axmax+axmin)).toInt()
        params.height = (myresizeVal*(aymax+aymin)).toInt()
        layout.layoutParams = params

        //for drawing the map
        val layout1 = layout1 as LinearLayout
        val canvass = Canvass(this, XMLActivity())



        layout1.addView(canvass)
        canvass.isFocusable = true





        canvass.setOnTouchListener(canvass)

        val zoomLinearLayout = zoom_linear_layout as ZoomLinearLayout
        zoomLinearLayout.setOnTouchListener { v, event ->
            //zoomLinearLayout.init(this@MapActivity)
            zoomLinearLayout.init(this)
            true
        }


        btn_first.setOnClickListener {
            val changePage = Intent(this, InfoActivity::class.java)
            startActivity(changePage)

        }

        btn_secondOne.setOnClickListener {
            val changePage = Intent(this, MainActivity::class.java)
            startActivity(changePage)
        }


    }


}
