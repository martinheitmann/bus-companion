package com.app.skyss_companion.view.alerts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActiveAlertsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
}