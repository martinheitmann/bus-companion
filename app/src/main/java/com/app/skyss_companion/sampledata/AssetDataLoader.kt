package com.app.skyss_companion.sampledata

import android.content.Context
import java.io.IOException

import java.io.InputStream


class AssetDataLoader {
    companion object {
        fun loadJSONFromAsset(context: Context, fileName: String): String? {
            var json: String? = null
            json = try {
                val `is`: InputStream = context.assets.open(fileName)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }
            return json
        }
    }
}