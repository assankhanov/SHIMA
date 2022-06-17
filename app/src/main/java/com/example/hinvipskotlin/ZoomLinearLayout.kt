package com.example.hinvipskotlin

import android.annotation.TargetApi
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.media.MediaPlayer

import android.os.Build
import android.os.VibrationEffect

import android.os.Vibrator

import android.speech.tts.TextToSpeech
import android.util.AttributeSet

import android.util.DisplayMetrics
import android.util.Log

import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import java.lang.Float.intBitsToFloat

import java.util.*

import kotlin.collections.ArrayList
import kotlin.math.abs
import android.view.ScaleGestureDetector
import android.view.View.OnTouchListener
import android.widget.LinearLayout




/**Canvass
 *
 * Class that draw something based on coordinate on a specified canvas
 *
 * @param context The Activity in which is setup CanvassRoom
 * @since 0.0.1
 *
 */
class ZoomLinearLayout : LinearLayout, ScaleGestureDetector.OnScaleGestureListener {

    private var mode = Mode.NONE
    private var scale = 1.0f
    private var lastScaleFactor = 0f

    private var startX = 0f
    private var startY = 0f

    private var dx = 0f
    private var dy = 0f
    private var prevDx = 0f
    private var prevDy = 0f

    private enum class Mode {
        NONE,
        DRAG,
        ZOOM
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    fun init(context: Context) {
        val scaleDetector = ScaleGestureDetector(context, this)
        this.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> if (scale > MIN_ZOOM) {
                    mode = Mode.DRAG
                    startX = motionEvent.x - prevDx
                    startY = motionEvent.y - prevDy
                }
                MotionEvent.ACTION_MOVE -> if (mode == Mode.DRAG) {
                    dx = motionEvent.x - startX
                    dy = motionEvent.y - startY
                }
                MotionEvent.ACTION_POINTER_DOWN -> mode = Mode.ZOOM
                MotionEvent.ACTION_POINTER_UP -> mode = Mode.DRAG
                MotionEvent.ACTION_UP -> {
                    mode = Mode.NONE
                    prevDx = dx
                    prevDy = dy
                }
            }
            scaleDetector.onTouchEvent(motionEvent)

            if (mode == Mode.DRAG && scale >= MIN_ZOOM || mode == Mode.ZOOM) {
                parent.requestDisallowInterceptTouchEvent(true)
                val maxDx = (child().width - child().width / scale) / 2 * scale
                val maxDy = (child().height - child().height / scale) / 2 * scale
                dx = Math.min(Math.max(dx, -maxDx), maxDx)
                dy = Math.min(Math.max(dy, -maxDy), maxDy)
                applyScaleAndTranslation()
            }

            true
        }
    }

    override fun onScaleBegin(scaleDetector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScale(scaleDetector: ScaleGestureDetector): Boolean {
        val scaleFactor = scaleDetector.scaleFactor
        if (lastScaleFactor == 0f || Math.signum(scaleFactor) == Math.signum(lastScaleFactor)) {
            scale *= scaleFactor
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM))
            lastScaleFactor = scaleFactor
        } else {
            lastScaleFactor = 0f
        }
        return true
    }

    override fun onScaleEnd(scaleDetector: ScaleGestureDetector) {}

    private fun applyScaleAndTranslation() {
        child().scaleX = scale
        child().scaleY = scale
        child().translationX = dx
        child().translationY = dy
    }

    private fun child(): View {
        return getChildAt(0)
    }

    companion object {

        private val MIN_ZOOM = 1.0f
        private val MAX_ZOOM = 4.0f
    }

}