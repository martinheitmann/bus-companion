package com.app.skyss_companion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.app.skyss_companion.misc.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create the notification channel
        // Can safely call this multiple times.
        NotificationUtils.createNotificationChannel(
            applicationContext,
            applicationContext.getString(R.string.channel_id),
            applicationContext.getString(R.string.channel_name),
            applicationContext.getString(R.string.channel_desc),
            NotificationCompat.PRIORITY_MAX
        )
        setContentView(R.layout.activity_main)
    }
}