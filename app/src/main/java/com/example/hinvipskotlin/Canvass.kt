package com.example.hinvipskotlin

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.media.MediaPlayer

import android.os.Build
import android.os.VibrationEffect

import android.os.Vibrator

import android.speech.tts.TextToSpeech

import android.util.DisplayMetrics
import android.util.Log

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_map.view.*
import java.lang.Float.intBitsToFloat

import java.util.*

import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.roundToInt
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent




/**Canvass
 *
 * Class that draw something based on coordinate on a specified canvas
 *
 * @param context The Activity in which is setup Canvass
 * @since 0.0.1
 *
 */
class Canvass(context: Context, private var xmlparser: XMLActivity): View(context), View.OnTouchListener, TextToSpeech.OnInitListener {



    // Definition of variables that will be used in the class
    private var paint = Paint() // Define the parameters of the lines used for the drawing
    private var polygons = arrayListOf<Figure>() // Contains room as Figures
    private var lines = arrayListOf<RectF>() // Contains walls as RectF
    private var doors = arrayListOf<RectF>() // Contains doors as RectF
    private var mediaPlayerDoor : MediaPlayer  = MediaPlayer.create(context, R.raw.door) // Define the song that will be played for doors
    private var mediaPlayerWall : MediaPlayer  = MediaPlayer.create(context, R.raw.wall) // Define the song that will be played for walls
    private var vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator // Check the availability of a vibration motor

    private var tts = TextToSpeech(context, this) // Initialisation of the text to speech service
    private var displayMetrics = DisplayMetrics()


    private var door = false //boolean to define doors
    private var cell = false// boolean to define if it is a floor cell
    private var touch = false //boolean that define if the action is the continuation of a single touch



    override fun onInit(status: Int) {



        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // get device dimensions
        wm.defaultDisplay.getMetrics(displayMetrics)

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            }

        }
        else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    /**onDraw
     *
     * override onDraw from view
     * Define what will be displayed on the specified canvas
     *
     * @param canvas canvas of the Activity the class has been called upon
     * @since 0.0.1
     *
     */
    override fun onDraw(canvas: Canvas) {

        var widthScreen = displayMetrics.widthPixels
        var heightScreen = displayMetrics.heightPixels

        //var width: Float = (widthScreen/3).toFloat()
        //var height:Float = (heightScreen/14).toFloat()
        //canvas.translate(150f,20f)
        //canvas.scale(0.09f,0.09f)



        // Draw the parts of the map
        drawCells(canvas)
        drawBoundaries(canvas)




    }

    /**draw Boundaries
     *
     * Draw the boundaries elements of the map such as walls and give them a visual hitbox.
     * @param canvas the Canvas where the elements are going to be drawn
     *
     */
    private fun drawBoundaries(canvas: Canvas){

        val aymax = abs(xmlparser.ymax)
        val axmin = abs(xmlparser.xmin)

        val axmax =abs(xmlparser.xmax)

        // Getting the coordinate of space or bounds from the xmlparser
        val myPath = this.xmlparser.getBound()

        //Log.e("msg", myPath.size.toString())

        // Setting the design of the display
        paint = Paint()

        paint.color = Color.RED

        paint.strokeWidth = 5f

        paint.style = Paint.Style.STROKE

        // Setting the variables that will holds the transformed coordinate for displaying
        var x1 : Float
        var x2 : Float
        var y1 : Float
        var y2 : Float




        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(displayMetrics)

        //var widthScreen = displayMetrics.widthPixels




        // Getting the maximums and minimums of the map
        //var widthScreen = displayMetrics.widthPixels
        val heightScreen = displayMetrics.heightPixels
        val widthScreen = displayMetrics.widthPixels



        val resizeVal = if(aymax<=axmax){
            (widthScreen/(axmax*1.1)).toFloat()
        }
        else {
            (heightScreen/(aymax*1.6)).toFloat()
        }



        // Debug for the dimension of the screen
        //Log.e("width", String.format(" %d %f %d %f", "Width and height",axmin,heightScreen,aymax))


        // Iterating through bounds
        for (k in myPath.indices) {

            //REinitialize the path for every group of coordinate.
            val dpath = Path()




            // Buffer for the variable
            var x = myPath[k][0].x
            var y = myPath[k][0].y


            // Scaling of the variables
            y = when (y < 0) {
                true -> (abs(y) + aymax) * resizeVal
                false -> abs(y - aymax) * resizeVal
            }
            x = (x + abs(axmin)) * resizeVal


            // Moving to the starting point for our polygon without drawing
            dpath.moveTo(x, y)
            //Log.e("msh", x.toString())


            for (l in 1 until myPath[k].size) {
                //Log.e("Pos count", String.format("%s %d", "MyPoints", myPath.get(k).size))
                // Stocking the Points for the RectF
                val x0 = x
                val y0 = y

                // Defining the next set of points
                x = myPath[k][l].x
                y = myPath[k][l].y

                // Scaling of the variables
                y = when (y < 0) {
                    true -> (abs(y) + aymax) * resizeVal
                    false -> abs(y - aymax) * resizeVal
                }
                x = (x + abs(axmin)) * resizeVal


                // Sorting of the variables for the definition of the RectF
                if (x0 < x) {
                    x1 = x0
                    x2 = x
                } else {
                    x2 = x0
                    x1 = x
                }
                if (y0 < y) {
                    y1 = y0
                    y2 = y

                } else {
                    y1 = y
                    y2 = y0
                }

                // Creation of the RectF
                val r = when(abs(x2) - abs(x1) < abs(y2) - abs(y1)){
                    true ->  RectF(x1 - 10f, y1 , x2 + 10f, y2 )
                    false -> RectF(x1 , y1 - 10f, x2 , y2 + 10f)
                }




                // Adding the RectF to the doors and drawing
                doors.add(r)
                canvas.drawRect(r,paint)

                // Drawing of the line that is the door
                dpath.lineTo(x, y)



            }

        }

    }



    /**drawCells
     *
     * Draw the cells elements of the map such as walls and give them a visual hitbox.
     * Uses RectF to give them Touch property
     * @param canvas the Canvas where the elements are going to be drawn
     *
     */
    private fun drawCells(canvas: Canvas){





        // Getting the maximums and minimums of the map for scaling
        val aymax = abs(xmlparser.ymax)
        val axmin = abs(xmlparser.xmin)
        val axmax =abs(xmlparser.xmax)







        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val names = this.xmlparser.getNames()

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(displayMetrics)

        //var widthScreen = displayMetrics.widthPixels
        val heightScreen = displayMetrics.heightPixels
        val widthScreen = displayMetrics.widthPixels

        //val resizeVal = (widthScreen/(axmax*1.1)).toFloat()


        val resizeVal = if(aymax<=axmax){
            (widthScreen/(axmax*1.1)).toFloat()
        }
        else {
            (heightScreen/(aymax*1.6)).toFloat()
        }




       // when(heightScreen>widthScreen)
       // {
       //     resizeVal = 100f
        //}


        Log.e("resize val", String.format("HEIGHT: %d WIDTH: %d  ayMAX: %f axMIN: %f axMAX:%f",heightScreen,widthScreen,aymax,axmin, axmax))

        // Getting the coordinate of space or bounds from the xmlparser
        val myPath = this.xmlparser.getSpace()


        // Setting the design of the display
        paint = Paint()

        paint.color = Color.BLACK

        paint.strokeWidth = 8f

        paint.style = Paint.Style.STROKE


        // Variables that will hold the transformed values for display
        var x1 : Float
        var x2 : Float
        var y1 : Float
        var y2 : Float



        // Iterating through space or bounds
        for (k in myPath.indices) {
            //Log.e("Cellspace count", String.format("%s %d", "MyPoints", myPath.size))

            // Reinitialising the path and polygon
            val polygon = ArrayList<Edge>()
            val dpath = Path()

            // Getting the values of the point on the map
            var x = myPath[k][0].x
            var y = myPath[k][0].y


            // Adapting the value to the android display

            y = when (y < 0) {
                true -> (abs(y) + aymax) * resizeVal
                false -> abs(y - aymax) * resizeVal
            }
            x = (x + abs(axmin)) * resizeVal


            // Moving without drawing to the starting point of the polygon
            dpath.moveTo(x, y)
            //Log.e("msh", x.toString())

            // Iterating inside the polygon
            for (l in 1 until myPath[k].size) {
                //Log.e("Pos count", String.format("%s %d", "MyPoints", myPath.get(k).size))

                // Buffering the values for the creation of the RectF
                val x0 = x
                val y0 = y

                // Retrieving the next values
                x = myPath[k][l].x
                y = myPath[k][l].y

                // Adapting values for the display
                y = when (y < 0) {
                    true -> (abs(y) + aymax) * resizeVal
                    false -> abs(y - aymax) * resizeVal
                }
                x = (x + abs(axmin)) * resizeVal


                // Adding the line to the polygon
                polygon.add(Edge(PointF(x0, y0), PointF(x, y)))


                // Sorting of the coordinates for the creation of the rectF
                if (x0 < x) {
                    x1 = x0
                    x2 = x
                } else {
                    x2 = x0
                    x1 = x
                }
                if (y0 < y) {
                    y1 = y0
                    y2 = y

                } else {
                    y1 = y
                    y2 = y0
                }

                val r = RectF(x1 - 5f, y1 - 5f, x2 + 5f, y2 + 5f)


                // Adding the rectF that will define  the wall
                lines.add(r)
                //canvas.drawRect(r,paint)

                // Drawing the line that will represent the wall
                dpath.lineTo(x, y)

            }

            // Creating a holder for the list of edge we created earlier
            val array = arrayOfNulls<Edge>(polygon.size)


            // Buffering the past values
            val x0 = x
            val y0 = y

            // Fetching the first values to close the polygon
            x = myPath[k][0].x
            y = myPath[k][0].y

            // Adapting the values to the display
            y = when (y < 0) {
                true -> (abs(y) + aymax) * resizeVal
                false -> abs(y - aymax) * resizeVal
            }
            x = (x + abs(axmin))* resizeVal


            // Adding the last Edge to our polygon and drawing the closing the drawing
            polygon.add(Edge(PointF(x0, y0), PointF(x, y)))
            dpath.lineTo(x, y)


            // Sorting the values for the creation of the RectF
            if (x0 < x) {
                x1 = x0
                x2 = x
            } else {
                x2 = x0
                x1 = x
            }
            if (y0 < y) {
                y1 = y0
                y2 = y

            } else {
                y1 = y
                y2 = y0
            }


            // Creation of the Rect that will make the last wall
            val r = RectF(x1 - 5f, y1 - 5f, x2 + 5f, y2 + 5f)


            // Adding the wall to the list with all the walls
            lines.add(r)
            //canvas.drawRect(r,paint)

            // Creating an Array from the ArrayList that we created through the loop
            val list: Array<Edge> = polygon.toArray(array)
            //Log.e("MSG", list.toString())

            // Adding the polygon to the list of all the polygons
            polygons.add(Figure(names[k], list))




            // Drawing the polygon we defined in the loop
            canvas.drawPath(dpath, paint)



        }

    }


    /**onHoverEvent
     *
     * The override is to allow OnTouch Event to be used when accessibility services are enabled
     *
     * @param event Id the MotionsEvent detected by the activity
     *
     *
     */
    override fun onHoverEvent(event :MotionEvent): Boolean{
        // get the state of te accesibility service
        val am : AccessibilityManager =  context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        // Answer accordingly to this state
        return if (am.isTouchExplorationEnabled) {
            onTouch(View(context),event)
        } else {
            super.onHoverEvent(event)
        }
    }


    /**onTouch
     *
     * Available only since Marshmallow
     * Is taking care of the answer when the screen Touch
     *
     */
    @TargetApi(Build.VERSION_CODES.M)
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        // Coordinate of the TouchEvent
        val x = motionEvent.x
        val y = motionEvent.y

        // Check as often as possible the current duration of the door sound
        if( mediaPlayerDoor.currentPosition > 100) {
            mediaPlayerDoor.pause()
            mediaPlayerDoor.seekTo(0)
        }



        // Handle the MotionEvent
        when (motionEvent.actionMasked) {
            // Every Event is double with his hover counterpart for when the accessibility service is enabled
            MotionEvent.ACTION_MOVE ,MotionEvent.ACTION_HOVER_MOVE-> {
                // For doors
                doors.forEach {
                    if(it.contains(x,y)){
                        // Check if it touches a door and if it is continuous to a touch event
                        if(!door && !touch){
                            door = true
                            cell =false
                            Log.e("FeedBack", "Door")
                            //tts.speak("Door", TextToSpeech.QUEUE_FLUSH, null,"")
                            mediaPlayerDoor.start()
                            vibe.cancel()


                        }


                        touch = false



                        return true
                    }
                }

                //EventListener for walls
                lines.forEach {

                    if (it.contains(x, y)) {
                        cell =false
                        door =false
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibe?.vibrate(VibrationEffect.createOneShot(5000000, 255))
                            mediaPlayerWall.start()
                        } else {
                            vibe?.vibrate(5000000)
                            mediaPlayerWall.start()
                        }


                        Log.e("FeedBack", "Wall")



                        return true
                    }
                }

                polygons.forEach {
                    if (it.contains(PointF(x, y))) {
                        if (!cell ){
                            cell = true
                            door = false
                            if( mediaPlayerDoor.currentPosition > 500) {
                                mediaPlayerDoor.pause()
                                mediaPlayerDoor.seekTo(0)
                            }
                            vibe.cancel()


                            var name  = it.name
                            var newName = name.replace("Cell", "Room")

                            tts.speak(newName, TextToSpeech.QUEUE_FLUSH, null,"")

                            Log.e("FeedBack", it.name)

                        }
                        touch = false






                        return true
                    }
                }


            }

            MotionEvent.ACTION_DOWN,MotionEvent.ACTION_HOVER_ENTER-> {
                doors.forEach {
                    if(it.contains(x,y)){
                        touch = true
                        cell =false
                        door = true
                        mediaPlayerDoor.start()
                        vibe.cancel()


                        Log.e("FeedBack", "Door")

                        return true
                    }
                }

                //EventListener for walls
                lines.forEach {

                    if (it.contains(x, y)) {
                        cell =false
                        door =false
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibe?.vibrate(VibrationEffect.createOneShot(5000000, 255))
                            mediaPlayerWall.start()
                        } else {
                            vibe?.vibrate(5000000)
                            mediaPlayerWall.start()
                        }
                        Log.e("FeedBack", "Wall")
                        return true
                    }

                }



                //Event Listener for Rooms
                polygons.forEach {
                    if (it.contains(PointF(x, y))) {
                        touch = true
                        cell =true
                        door = false


                        var name  = it.name
                        var newName = name.replace("Cell", "room")


                        tts.speak(newName, TextToSpeech.QUEUE_FLUSH, null,"")
                        vibe.cancel()

                        Log.e("FeedBack", it.name)



                        return true
                    }
                }




            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_HOVER_EXIT->{
                door = false
                cell =false

                vibe.cancel()
                return true

            }

        }
        // If nothing has been touch reset all switch
        touch = false
        cell = false
        door = false
        vibe.cancel()
        return true
    }

}
