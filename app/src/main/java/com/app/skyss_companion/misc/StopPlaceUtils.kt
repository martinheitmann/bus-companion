package com.app.skyss_companion.misc

import android.util.Log
import com.app.skyss_companion.model.Stop
import com.app.skyss_companion.view.stop_place.StopPlaceListDivider
import com.app.skyss_companion.view.stop_place.StopPlaceListEntry
import com.app.skyss_companion.view.stop_place.StopPlaceListItem

class StopPlaceUtils {
    companion object {

        val TAG = "StopPlaceUtils"

        fun createListData(stops: List<Stop>): List<StopPlaceListItem> {
            //Log.d(TAG, "${stops.size} stops passed to list generator")
            val items = mutableListOf<StopPlaceListItem>()
            stops.forEach { stop ->
                //Log.d(TAG, "Stop with name ${stop.description} contained  ${stop.routeDirections?.size ?: "null"} routes")
                val header = StopPlaceListDivider(stop.description ?: "Ingen beskrivelse")
                items.add(header)
                stop.routeDirections?.forEach { rd ->
                    val displayTimes = mutableListOf<String>()
                    val directionName = rd.directionName
                    val passingTimes = rd.passingTimes
                    //Log.d(TAG, "Found ${passingTimes?.size} passing times for route with name '${directionName}'")
                    passingTimes?.forEach { pt ->
                        val display = pt.displayTime
                        if (display != null) {
                            displayTimes.add(display)
                        }
                    }
                    val listItem = StopPlaceListEntry(
                        stopIdentifier = stop.identifier,
                        routeDirectionIdentifier = rd.identifier,
                        lineNumber = rd.publicIdentifier ?: "0",
                        directionName = directionName ?: "Ukjent rute",
                        displayTimes = displayTimes,
                        isEmphasized = passingTimes?.map { pt -> pt.status == "OnTime" }
                            ?: mutableListOf()
                    )
                    items.add(listItem)
                }
            }
            return items
        }

        fun createFilteredListData(
            stops: List<Stop>,
            lineCodes: List<String>
        ): List<StopPlaceListItem> {
            val items = mutableListOf<StopPlaceListItem>()
            stops.forEach { stop ->
                val relevantRouteDirections = stop.routeDirections?.filter { routeDirection ->
                    lineCodes.contains(routeDirection.publicIdentifier)
                }?.toList()
                if (relevantRouteDirections != null) {
                    if (relevantRouteDirections.isNotEmpty()) {
                        val header = StopPlaceListDivider(stop.description ?: "Ingen beskrivelse")
                        items.add(header)
                        relevantRouteDirections.forEach { rd ->
                            val displayTimes = mutableListOf<String>()
                            val directionName = rd.directionName
                            val passingTimes = rd.passingTimes
                            //Log.d(TAG, "Found ${passingTimes?.size} passing times for route with name '${directionName}'")
                            passingTimes?.forEach { pt ->
                                val display = pt.displayTime
                                if (display != null) {
                                    displayTimes.add(display)
                                }
                            }
                            val listItem = StopPlaceListEntry(
                                stopIdentifier = stop.identifier,
                                routeDirectionIdentifier = rd.identifier,
                                lineNumber = rd.publicIdentifier ?: "0",
                                directionName = directionName ?: "Ukjent rute",
                                displayTimes = displayTimes,
                                isEmphasized = passingTimes?.map { pt -> pt.status == "OnTime" }
                                    ?: mutableListOf()
                            )
                            items.add(listItem)
                        }
                    }
                }
            }
            return items
        }

    }
}