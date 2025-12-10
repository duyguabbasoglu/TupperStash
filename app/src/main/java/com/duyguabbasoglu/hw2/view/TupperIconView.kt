package com.duyguabbasoglu.hw2.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class TupperIconView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    fun setTupperColor(colorCode: String) {
        try {
            this.setColorFilter(Color.parseColor(colorCode))
        } catch (e: Exception) {
            this.setColorFilter(Color.GRAY)
        }
    }
}