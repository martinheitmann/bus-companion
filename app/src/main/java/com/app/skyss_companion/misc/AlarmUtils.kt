package com.app.skyss_companion.misc

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

/**
 * Class for setting and removing alarms.
 */
class AlarmUtils {
    companion object {

        /**
         * Sets an alarm (using AlarmManager) with the argument intent and
         * trigger time milliseconds.
         *
         * The alarm trigger time will be set to the current system millis + triggerAtMillis.
         */
        fun setAlarm(
            context: Context,
            requestCode: Int,
            intent: Intent,
            triggerAtMillis: Long
        ): PendingIntent {
            val pendingIntent =
                NotificationUtils.createNotificationPendingIntent(context, 0, intent)
            (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            return pendingIntent
        }

        /**
         * Removes an alarm. An intent identical to the one which is to be removed
         * must be provided.
         */
        fun removeAlarm(context: Context, intent: Intent) {
            val pendingIntent =
                NotificationUtils.createNotificationPendingIntent(context, 0, intent)
            (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.cancel(pendingIntent)
        }
    }

}