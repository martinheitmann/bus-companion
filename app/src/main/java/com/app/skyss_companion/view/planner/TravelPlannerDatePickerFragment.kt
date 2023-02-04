package com.app.skyss_companion.view.planner

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

class TravelPlannerDatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    val mTag = "TPDatePickerFrag"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val bundle = Bundle()
        bundle.putInt("year", year)
        bundle.putInt("month", month + 1)
        bundle.putInt("day", day)
        Log.d(mTag, "onDateSet setting bundle with with year,month,day = $year,$month,$day")
        setFragmentResult(FragmentReturnType.DATE.type, bundle)
        // TS=2022-08-06T18%3A29
    }
}