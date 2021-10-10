package com.app.skyss_companion.view.routedirection_timetable

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import android.view.View

import android.widget.Button

import android.view.ViewGroup

import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.DateUtils
import java.time.ZonedDateTime


class SetAlertDialogFragment(private val passingTimeListItem: PassingTimeListItem) : DialogFragment() {

    lateinit var passingTimeTimeView: TextView
    lateinit var passingTimeDateView: TextView
    lateinit var errorTextView: TextView
    lateinit var calculatedTextView: TextView
    lateinit var inputView: EditText
    lateinit var submitButton: Button
    lateinit var closeButton: Button

    private lateinit var listener: SetAlertDialogListener

    interface SetAlertDialogListener {
        fun onDialogPositiveClick(
            passingTimeListItem: PassingTimeListItem,
            inputMinutes: Int
        )
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    fun setListener(context: SetAlertDialogListener){
        this.listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_set_alert, container, false)
        inputView = view.findViewById(R.id.dialog_set_alert_edit_text)
        passingTimeDateView = view.findViewById(R.id.dialog_set_alert_date)
        passingTimeTimeView = view.findViewById(R.id.dialog_set_alert_time)
        closeButton = view.findViewById(R.id.dialog_set_alert_cancel)
        submitButton = view.findViewById(R.id.dialog_set_alert_confirm)
        errorTextView = view.findViewById(R.id.dialog_set_alert_error)
        calculatedTextView = view.findViewById(R.id.dialog_set_alert_time_calculated)
        submitButton.isEnabled = false
        calculatedTextView.visibility = View.GONE
        errorTextView.visibility = View.GONE

        closeButton.setOnClickListener { closeDialog() }
        submitButton.setOnClickListener { submit() }

        val utcZdt = DateUtils.convertLocalDateTimeToUtcZonedDateTime(passingTimeListItem.timeStamp)
        val zdt = DateUtils.changeZoneForZonedDateTime(utcZdt)

        val dateString = "${String.format("%02d", zdt.dayOfMonth)}.${String.format("%02d", zdt.monthValue)}.${zdt.year}"
        val timeString = "${String.format("%02d", zdt.hour)}:${String.format("%02d", zdt.minute)}"

        passingTimeDateView.text = dateString
        passingTimeTimeView.text = timeString

        inputView.doOnTextChanged { text, _, _, _ ->
            try {
                val num = Integer.parseInt(text.toString())
                val isValid = num >= 5 && isAfterNow(zdt, num)
                val triggerTime = zdt.minusMinutes(num.toLong())
                if(isValid){
                    submitButton.isEnabled = true
                    errorTextView.visibility = View.GONE
                    calculatedTextView.text = setNotificationTimeText(triggerTime.hour, triggerTime.minute)
                    calculatedTextView.visibility = View.VISIBLE
                } else {
                    submitButton.isEnabled = false
                    errorTextView.visibility = View.VISIBLE
                    calculatedTextView.visibility = View.GONE
                }
            } catch(e: Throwable){
                submitButton.isEnabled = false
                if(text != null && text.isEmpty()){
                    calculatedTextView.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }
            }
        }
        return view
    }

    /**
     * Checks if the provided integer in combination with the departure time results in
     * a trigger time before ZonedDateTime.now() + margin.
     */
    private fun isAfterNow(departureTime: ZonedDateTime, minutesBefore: Int, margin: Long = 1): Boolean{
        val minutesAsLong = minutesBefore.toLong()
        if(departureTime.minusMinutes(minutesAsLong).isBefore(ZonedDateTime.now().plusMinutes(margin))){
            return false
        }
        return true
    }

    private fun setNotificationTimeText(hour: Int, minute: Int): String{
        return "Notifikasjonen sendes ${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }

    private fun closeDialog(){
        listener.onDialogNegativeClick(this)
        dismiss()
    }

    private fun submit(){
        val input = inputView.text.toString()
        val num = Integer.parseInt(input)
        listener.onDialogPositiveClick(passingTimeListItem, num)
        dismiss()
    }
}