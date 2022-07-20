package com.app.skyss_companion.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavDeepLinkBuilder
import com.app.skyss_companion.R
import com.app.skyss_companion.view.planner.selected_plan.SelectedTravelPlanFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

@AndroidEntryPoint
class TravelPlannerWidgetBroadcastReceiver : BroadcastReceiver() {

    val tag = "TPWidgetBroadcastReceiver"

    private val broadcastReceiverJob = Job()
    private val broadcastReceiverScope = CoroutineScope(Dispatchers.Main + broadcastReceiverJob)

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let {
            when (p1?.action) {
                Intent.ACTION_BOOT_COMPLETED -> {
                    Log.d(tag, "Intent action BOOT_COMPLETED received.")
                }
                Intent.ACTION_INPUT_METHOD_CHANGED -> {
                    Log.d(tag, "Intent action INPUT_METHOD_CHANGED received.")
                }
                else -> startSelectedTravelPlannerActivity(p0, p1?.getStringExtra("travelPlanId"))
            }
        } ?: run {
            Log.d(tag, "Argument context was null, ending broadcast receiver execution.")
        }
    }

    private fun startSelectedTravelPlannerActivity(context: Context, id: String?) {
        id?.let { tpId ->
            /*val intent = Intent(context, SelectedTravelPlanFragment::class.java).apply {
                putExtra("travelPlanId", tpId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)*/
            val pendingIntent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.selectedTravelPlanFragment)
                .setArguments(Bundle().apply {
                    putString("travelPlanId", tpId)
                })
                .createPendingIntent()
            pendingIntent.send()
        } ?: run {
            Log.d(tag, "Travel plan id was null, ending broadcast receiver execution.")
        }
    }
}