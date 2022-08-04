package com.app.skyss_companion.view.planner

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import java.time.LocalDateTime
import java.util.*

class TravelPlannerTimePickerFragment(private val partialDate: LocalDateTime) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    val mTag = "TPTimePickerFrag"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        val dateWithTime = partialDate
            .withHour(hour)
            .withMinute(minute)
            .withSecond(0)
        val dateWithTimeString = dateWithTime.toString()
        val bundle = Bundle()
        bundle.putInt("hour", hour)
        bundle.putInt("minute", minute)
        bundle.putString("fullDate", dateWithTimeString)
        Log.d(
            mTag,
            "onTimeSet setting bundle with with hour,minute, fullDate = $hour,$minute, $dateWithTimeString"
        )
        setFragmentResult(FragmentReturnType.TIME.type, bundle)
    }
}