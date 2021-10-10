package com.app.skyss_companion.misc

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.skyss_companion.MainActivity
import java.time.LocalDateTime
import android.text.SpannableString

import android.text.Spannable
import android.text.style.StyleSpan
import android.app.Notification
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import com.app.skyss_companion.R
import com.app.skyss_companion.broadcastreceivers.AlertNotificationBroadcastReceiver
import com.app.skyss_companion.view.routedirection_timetable.RouteDirectionTimeTableFragment
import java.time.ZonedDateTime


class NotificationUtils {

    companion object {

        /**
         * Creates and sends a notification.
         */
        fun createNotification(
            context: Context,
            channelId: String,
            contentTitle: String,
            contentText: String,
            priority: Int,
            pendingIntent: PendingIntent,
            notificationId: Int
        ) {
            val sb: Spannable = SpannableString(contentTitle)
            sb.setSpan(StyleSpan(Typeface.BOLD), 0, sb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_baseline_directions_bus_24)
                .setContentTitle(sb) // Using Spannable for bold title.
                .setContentText(contentText)
                .setStyle(
                    NotificationCompat.BigTextStyle() // Expandable notification style
                        .bigText(contentText)
                )
                .setPriority(priority) // Should be at least high.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        }

        /**
         * Creates a notification channel for delivering notifications
         * on more recent versions of Android.
         * Should be called as early as possible into the application's execution.
         */
        fun createNotificationChannel(
            context: Context,
            channelId: String,
            channelName: String,
            channelDesc: String,
            channelImportance: Int
        ) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, channelName, channelImportance).apply {
                    description = channelDesc
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        /**
         * Creates a PendingIntent for performing a broadcast using the
         * PendingIntent.getBroadcast() method. Sets the argument intent
         * as the PendingIntent intent.
         */
        @SuppressLint("UnspecifiedImmutableFlag")
        fun createNotificationPendingIntent(
            context: Context,
            requestCode: Int,
            intent: Intent
        ): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                0
            )
        }

        /**
         * Creates the intent for the alert notification interaction. Sends the user to
         * the MainActivity if any of the arguments are null. Otherwise, sends the user to
         * the route direction table.
         */
        fun createAlertNotificationClickActionIntent(
            context: Context,
            stopIdentifier: String? = null,
            routeDirectionIdentifier: String? = null,
            stopName: String? = null,
            directionName: String? = null,
            lineNumber: String? = null
        ): Intent {
            return if (stopIdentifier != null && routeDirectionIdentifier != null && stopName != null && directionName != null && lineNumber != null){
                val intent = Intent(context, RouteDirectionTimeTableFragment::class.java)
                intent.putExtra("STOP_IDENTIFIER", stopIdentifier)
                intent.putExtra("ROUTE_DIRECTION_IDENTIFIER", routeDirectionIdentifier)
                intent.putExtra("STOPGROUP_NAME", stopName)
                intent.putExtra("ROUTE_DIRECTION_NAME", directionName)
                intent.putExtra("LINE_NUMBER", lineNumber)
                intent
            } else Intent(context, MainActivity::class.java)
        }

        /**
         * Creates the intent for invoking the AlertNotification broadcast receiver.
         */
        fun createAlertNotificationIntent(context: Context): Intent {
            return Intent(context, AlertNotificationBroadcastReceiver::class.java)
        }

        /**
         * Creates the intent for invoking the AlertNotification broadcast receiver.
         * Also allows for the recreation of an intent for alarm
         * cancellation in the case where all arguments are known.
         */
        fun createAlertNotificationIntent(
            context: Context,
            passingTimeAlertId: Long,
            tripIdentifier: String,
            stopIdentifier: String,
            routeDirectionIdentifier: String,
            stopName: String,
            lineNumber: String,
            departureHour: Int,
            departureMinute: Int,
            directionName: String,
        ): Intent {
            val intent = Intent(context, AlertNotificationBroadcastReceiver::class.java)
            /* If a unique action isn't set, the intent extras may be duplicated for following messages */
            intent.action = passingTimeAlertId.toString()
            intent.putExtra("passingTimeAlertId", passingTimeAlertId)
            intent.putExtra("tripIdentifier", tripIdentifier)
            intent.putExtra("stopIdentifier", stopIdentifier)
            intent.putExtra("routeDirectionIdentifier", routeDirectionIdentifier)
            intent.putExtra("stopName", stopName)
            intent.putExtra("lineNumber", lineNumber)
            intent.putExtra("departureHour", departureHour)
            intent.putExtra("departureMinute", departureMinute)
            intent.putExtra("directionName", directionName)
            return intent
        }

        /**
         * Formats the notification text from a ZonedDateTime.
         */
        fun getAlertNotificationTextZdt(
            lineNumber: String,
            stopName: String,
            date: ZonedDateTime
        ): String {
            return "Linje $lineNumber har avgang fra $stopName kl. ${
                String.format(
                    "%02d",
                    date.hour
                )
            }:${String.format("%02d", date.minute)}. Trykk her for å åpne rutetabellen."
        }

        /**
         * Formats the notification text from a date string.
         */
        fun getAlertNotificationTextPreformatted(
            lineNumber: String,
            stopName: String,
            date: String
        ): String {
            return if (date.contains("min")) {
                "Linje $lineNumber har avgang fra $stopName om ${date}. Trykk her for å åpne rutetabellen."
            } else "Linje $lineNumber har avgang fra $stopName kl. ${date}. Trykk her for å åpne rutetabellen."
        }

        /**
         * Creates a formatted notification title string.
         */
        fun getAlertNotificationTitle(lineNumber: String, stopName: String): String {
            return "Linje $lineNumber fra $stopName"
        }
    }

}