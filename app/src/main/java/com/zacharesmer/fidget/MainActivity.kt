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

//
//class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
//    var hapticConstant = 0
//    lateinit var hapticNumberArray: IntArray
//
//    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
//        override fun onDown(e: MotionEvent?): Boolean {
//            print("down")
//            return true
//        }
//
//        override fun onFling(
//            e1: MotionEvent?,
//            e2: MotionEvent?,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//            return super.onFling(e1, e2, velocityX, velocityY)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // Set up spinner to pick a type of haptic feedback
//        val hapticSpinner: Spinner = findViewById(R.id.spinner)
//        val hapticArrayAdapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.haptic_name_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            hapticSpinner.adapter = adapter
//        }
//        // array of numbers that are the actual value of the constants
//        hapticNumberArray = resources.getIntArray(R.array.haptic_number_array)
//        hapticSpinner.onItemSelectedListener = this
//
//        // respond to the send/enter button
//        findViewById<EditText>(R.id.hapticNumberInput).setOnEditorActionListener { v, actionId, event ->
//            return@setOnEditorActionListener when (actionId) {
//                EditorInfo.IME_ACTION_SEND -> {
//                    hapticConstant = v.getText().toString().toInt()
//                    v.performHapticFeedback(hapticConstant)
//                    true
//                }
//                else -> false
//            }
//        }
//
//        // make an image that spins hopefully
//        val gestureDetector = GestureDetector(this, gestureListener)
//        val spinny_image = findViewById<ImageView>(R.id.spinny_image)
//        FlingAnimation(spinny_image, DynamicAnimation.TRANSLATION_X).apply {
//            setStartVelocity(30f)
//            setMinValue(0f)
//            setMaxValue(200f)
//            friction = 1.1f
//            start()
//        }
//    }
//
//    fun doHaptic(view: View) {
//        view.performHapticFeedback(hapticConstant)
////        println("Doing haptic %d".format(hapticConstant))
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
////        println(hapticNumberArray[position])
//        hapticConstant = hapticNumberArray[position].toString().toInt()
//        view?.performHapticFeedback(hapticConstant)
//        findViewById<EditText>(R.id.hapticNumberInput).setText(hapticConstant.toString())
//
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//        TODO("Not implemented")
//    }
//
//
//}

private const val DEBUG_TAG = "Gestures"

class MainActivity :
    Activity() {

    private lateinit var mDetector: GestureDetectorCompat
    lateinit var flingAnimation: FlingAnimation

    // Called when the activity is first created.
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener

        flingAnimation = FlingAnimation(findViewById(R.id.spinny_image), DynamicAnimation.ROTATION).apply{
            setStartVelocity(0f)
        }

        val gestureListener = object :GestureDetector.SimpleOnGestureListener() {
            override fun onDown(event: MotionEvent): Boolean {
//                Log.d(DEBUG_TAG, "onDown: $event")
                return true
            }

            override fun onFling(
                event1: MotionEvent,
                event2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                Log.d(DEBUG_TAG, "velocity x: $velocityX\nvelocity y: $velocityY")
//                Log.d(DEBUG_TAG, "motion event: ")
                flingAnimation.setStartVelocity(100f)
                flingAnimation.start()
                Log.d(DEBUG_TAG, "onFling: $event1 $event2")

                return true
            }
        }
        mDetector = GestureDetectorCompat(this, gestureListener)
        findViewById<ImageView>(R.id.spinny_image).setOnTouchListener {_, event -> mDetector.onTouchEvent(event)}

    }

//    public override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return if (mDetector.onTouchEvent(event)) {
//            true
//        } else {
//            super.onTouchEvent(event)
//        }
//    }
}
