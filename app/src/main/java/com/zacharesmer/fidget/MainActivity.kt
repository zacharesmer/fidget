package com.zacharesmer.fidget

import android.app.Activity
import android.graphics.ImageFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.view.GestureDetectorCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt



private const val DEBUG_TAG = "Gestures"

class MainActivity :
    Activity() {

    private lateinit var mDetector: GestureDetectorCompat
    lateinit var flingAnimation: FlingAnimation
    lateinit var spinny_image: ImageView

    // Called when the activity is first created.
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinny_image = findViewById(R.id.spinny_image)

        val scale = resources.displayMetrics.density
        val w = resources.displayMetrics.widthPixels
        val h = resources.displayMetrics.heightPixels
        Log.d(DEBUG_TAG, "width: $w, height: $h")
        val centerX = w/2
        val centerY = h/2
        Log.d(DEBUG_TAG, "center: $centerX $centerY")

        flingAnimation = FlingAnimation(spinny_image, DynamicAnimation.ROTATION).apply{
            setStartVelocity(0f)
            setFriction(0.5f)
        }

        flingAnimation.addUpdateListener(object : DynamicAnimation.OnAnimationUpdateListener {
            var last = 0f
            override fun onAnimationUpdate(
                animation: DynamicAnimation<*>?,
                value: Float,
                velocity: Float
            ) {
                // check if the spinner is within range of a rotation where it should do something
                // maybe instead check if it's moved more than some number of degrees, then fire,
                // then update the most recent position to a rounded value
                var normalized_rotation = spinny_image.rotation.roundToInt() % 90
                if (normalized_rotation < 10 && normalized_rotation > -10) {
//                    Log.d(DEBUG_TAG, "${normalized_rotation}")
                    // keep track of last absolute rotation to avoid duplicate haptics
                    if (abs(spinny_image.rotation - last) > 20) {
                        spinny_image.performHapticFeedback(HapticFeedbackConstants.TEXT_HANDLE_MOVE)
                    }
                    last = spinny_image.rotation
                }
            }
        })

        val gestureListener = object :GestureDetector.SimpleOnGestureListener() {
            override fun onDown(event: MotionEvent): Boolean {
//                Log.d(DEBUG_TAG, "onDown: $event")
//                Log.d(DEBUG_TAG, "touch: ${event.getRawX()-centerX}, ${event.getRawY()-centerY}")
                return true
            }

            override fun onFling(
                event1: MotionEvent,
                event2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {

                // coordinates of the events based around the center of the screen
                var x1 = event1.getRawX() - centerX
                var y1 = event1.getRawY() - centerY
                var x2 = event2.getRawX() - centerX
                var y2 = event2.getRawY() - centerY

                // calculate the x and y velocity of the fling by hand since Android does
                // it in some weird relative way
                var vX = (x2-x1) / ((event2.eventTime - event1.eventTime))
                var vY = (y2-y1) / ((event2.eventTime - event1.eventTime))


                // get the z component of the cross product F X r to find torque
                // F is approximated by the velocity and r is the point of contact
                var torque_magnitude = x1 * vY - y1 * vX
                // this is supposed to take care of the ill-defined mass and acceleration,
                // turns out it works okay as 1
                val force_tuning_constant = 1
                var velocity = torque_magnitude/force_tuning_constant
                flingAnimation.setStartVelocity(velocity)
                flingAnimation.start()

                return true
            }
        }
        mDetector = GestureDetectorCompat(this, gestureListener)
        spinny_image.setOnTouchListener {_, event -> mDetector.onTouchEvent(event)}

    }
}
