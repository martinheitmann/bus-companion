package com.app.skyss_companion.widget

import android.util.Log
import kotlin.math.round

class WidgetUtils {
    companion object {
        val TAG = "WidgetUtils"
        fun limitDisplayedItems(dpWidth: Int, dpHeight: Int) : Int{
            return when(((dpWidth + 30) / 70)){
                4 -> {
                    Log.d(TAG, "width $dpWidth resolved to 6 blocks")
                    6
                }
                3  -> {
                    Log.d(TAG, "width $dpWidth resolved to 4 blocks")
                    4
                }
                else -> {
                    Log.d(TAG, "width $dpWidth resolved to 3 blocks")
                    3
                }
            }
        }
    }
}