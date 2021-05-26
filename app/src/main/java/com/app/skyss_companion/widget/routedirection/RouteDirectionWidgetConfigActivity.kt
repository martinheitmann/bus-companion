package com.app.skyss_companion.widget.routedirection

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.databinding.WidgetViewPassingtimesConfigActivityBinding
import com.app.skyss_companion.widget.RouteDirectionAppWidgetProvider
import com.app.skyss_companion.widget.StopGroupAppWidgetProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RouteDirectionWidgetConfigActivity : AppCompatActivity() {
    val TAG = "RDidgetConfigActivity"
    var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    private lateinit var binding: WidgetViewPassingtimesConfigActivityBinding

    private val viewModel: RouteDirectionWidgetConfigViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: RouteDirectionWidgetConfigAdapter

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        Log.d(TAG, "onCreate")
        // Set the view layout resource to use.
        binding = WidgetViewPassingtimesConfigActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()

        // Set up recyclerview components
        recyclerView = binding.passingtimesConfigRecyclerview
        layoutManager = LinearLayoutManager(this)
        adapter = RouteDirectionWidgetConfigAdapter(::onItemSelected)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel.isLoading.observe(this, {
            if(it){
                binding.passingtimesConfigRecyclerview.visibility = View.GONE
                binding.passingtimesConfigProgressbar.visibility = View.VISIBLE
            } else {
                binding.passingtimesConfigRecyclerview.visibility = View.VISIBLE
                binding.passingtimesConfigProgressbar.visibility = View.GONE
            }
        })

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        viewModel.bookmarkedRouteDirections.observe(this, {
            adapter.setData(it)
        })
    }

    private fun onItemSelected(routeDirectionIdentifier: String, stopGroupIdentifier: String){
        if(mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
            viewModel.persistEnabledWidget(mAppWidgetId, stopGroupIdentifier, routeDirectionIdentifier, ::setupWidget)
        }
    }

    private fun setupWidget(appWidgetId: Int){
        // Push widget update to surface with newly set prefix
        RouteDirectionAppWidgetProvider().updateAppWidget(this, appWidgetId)
        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}