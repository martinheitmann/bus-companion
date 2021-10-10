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
import androidx.core.widget.doOnTextChanged
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.DateUtils


class SetAlertDialogFragment(private val passingTimeListItem: PassingTimeListItem) : DialogFragment() {

    lateinit var passingTimeTimeView: TextView
    lateinit var passingTimeDateView: TextView
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
        submitButton.isEnabled = false

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
                submitButton.isEnabled = num >= 10
            } catch(e: Throwable){
                submitButton.isEnabled = false
            }
        }

        return view
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