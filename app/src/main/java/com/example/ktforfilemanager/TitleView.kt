package com.example.ktforfilemanager

import android.app.Activity
import android.R
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout


class TitleView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val leftButton: Button

    private val titleText: TextView

    init {
        LayoutInflater.from(context).inflate(com.example.ktforfilemanager.R.layout.title, this)
        titleText = findViewById<TextView>(com.example.ktforfilemanager.R.id.title_text)
        leftButton = findViewById<Button>(com.example.ktforfilemanager.R.id.button_left) as Button
        leftButton.setOnClickListener({
            (getContext() as Activity).finish()
        })
    }

    fun setTitleText(text: String) {
        titleText.text = text
    }

    fun setLeftButtonText(text: String) {
        leftButton.setText(text)
    }

    fun setLeftButtonListener(l: View.OnClickListener) {
        leftButton.setOnClickListener(l)
    }

}