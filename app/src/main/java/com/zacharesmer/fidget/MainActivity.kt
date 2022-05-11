package com.zacharesmer.fidget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var hapticConstant = 0
    lateinit var hapticNumberArray : IntArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Set up spinner to pick a type of haptic feedback
        val hapticSpinner: Spinner = findViewById(R.id.spinner)
        val hapticArrayAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.haptic_name_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            hapticSpinner.adapter = adapter
        }
        // array of numbers that are the actual value of each variable
        hapticNumberArray = resources.getIntArray(R.array.haptic_number_array)
        hapticSpinner.onItemSelectedListener = this
    }

    fun doHaptic(view: View) {
        view.performHapticFeedback(hapticConstant)
//        println("Doing haptic %d".format(hapticConstant))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        println(hapticNumberArray[position])
        hapticConstant = hapticNumberArray[position].toString().toInt()
        view?.performHapticFeedback(hapticConstant)

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        println("nothing selected")
    }


}