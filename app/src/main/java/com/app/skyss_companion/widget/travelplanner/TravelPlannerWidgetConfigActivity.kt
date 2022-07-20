package com.app.skyss_companion.widget.travelplanner

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.databinding.WidgetFavoritesConfigBinding
import com.app.skyss_companion.databinding.WidgetTravelplansConfigBinding
import com.app.skyss_companion.widget.StopGroupAppWidgetProvider
import com.app.skyss_companion.widget.TravelPlannerAppWidgetProvider
import com.app.skyss_companion.widget.stopgroup.StopGroupWidgetConfigAdapter
import com.app.skyss_companion.widget.stopgroup.StopGroupWidgetConfigViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelPlannerWidgetConfigActivity : AppCompatActivity() {
    val mTag = "TPWidgetConfigActivity"
    var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    private lateinit var binding: WidgetTravelplansConfigBinding

    private val viewModel: TravelPlannerWidgetConfigViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: TravelPlannerWidgetConfigAdapter

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        Log.d(mTag, "onCreate")
        // Set the view layout resource to use.
        binding = WidgetTravelplansConfigBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()

        // Set up recyclerview components
        recyclerView = binding.widgetTravelplansConfigRecyclerview
        layoutManager = LinearLayoutManager(this)
        adapter = TravelPlannerWidgetConfigAdapter {
            onItemSelected(it)
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel.isLoading.observe(this) {
            if (it) {
                binding.widgetTravelplansConfigRecyclerview.visibility = View.GONE
                binding.widgetTravelplansConfigProgressbar.visibility = View.VISIBLE
            } else {
                binding.widgetTravelplansConfigRecyclerview.visibility = View.VISIBLE
                binding.widgetTravelplansConfigProgressbar.visibility = View.GONE
            }
        }

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

        viewModel.savedTravelPlans.observe(this) {
            adapter.setData(it)
        }
    }

    private fun onItemSelected(position: Int){
        if(mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
            viewModel.persistEnabledWidget(mAppWidgetId, position, applicationContext, ::setupWidget)
        }
    }

    private fun setupWidget(appWidgetId: Int){
        // Push widget update to surface with newly set prefix
        TravelPlannerAppWidgetProvider().updateAppWidget(this, appWidgetId)
        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}