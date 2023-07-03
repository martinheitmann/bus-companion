package com.app.skyss_companion.view.stop_place.model

data class StopGroupDeparturesEntry(
    /* val identifier: String,
    val lineCode: String,
    val description: String,
    val departures: List<String> */
    val lineNumber: String,
    val directionName: String,
    val displayTimes: List<String>,
    val routeDirectionIdentifier: String? = null,
    val stopIdentifier: String? = null,
    val stopName: String? = null,
    val routeDirectionName: String? = null
): StopGroupListItem()