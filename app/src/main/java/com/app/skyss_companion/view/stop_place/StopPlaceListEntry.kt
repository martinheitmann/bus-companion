package com.app.skyss_companion.view.stop_place

import java.time.LocalDateTime
import java.util.*

data class StopPlaceListEntry(
    val lineNumber: String,
    val directionName: String,
    val displayTimes: List<String>,
    val isEmphasized: List<Boolean>,
    val routeDirectionIdentifier: String? = null,
    val stopIdentifier: String? = null
) : StopPlaceListItem()