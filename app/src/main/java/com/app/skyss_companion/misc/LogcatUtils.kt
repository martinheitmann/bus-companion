package com.app.skyss_companion.misc

import android.util.Log

class LogcatUtils {
    companion object {

        /**
         * Prints a string to the Logcat in segments.
         */
        fun printStringInSegments(segmentSize: Int = 1000, tag: String, string: String){
            var str = string
            while(str.length > segmentSize){
                val subStr = str.take(segmentSize)
                Log.d(tag, subStr)
                str = str.substring(segmentSize, str.length)
            }
            Log.d(tag, str)
        }
    }
}