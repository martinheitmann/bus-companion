package com.app.skyss_companion.view.stop_place.model

data class StopGroupDeparturesEntry(
    val identifier: String,
    val lineCode: String,
    val description: String,
    val departures: List<String>
): StopGroupListItem()