package com.app.skyss_companion.widget

import android.content.Intent
import androidx.core.app.JobIntentService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class WidgetUpdateService : JobIntentService() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onHandleWork(intent: Intent) {
        val appWidgetId = intent.extras?.get("APP_WIDGET_ID") as Int?
        appWidgetId?.let { id ->

        }
    }

}