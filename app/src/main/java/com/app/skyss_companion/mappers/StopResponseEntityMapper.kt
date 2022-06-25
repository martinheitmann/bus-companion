package com.app.skyss_companion.mappers

import com.app.skyss_companion.http.model.*
import com.app.skyss_companion.model.*

class StopResponseEntityMapper {
    companion object {
        fun mapStopGroupResponse(stopGroup: StopGroupResponse): StopGroup? {
            val ident = stopGroup.Identifier
            val desc = stopGroup.Description
            val loc = stopGroup.Location
            val sm1 = stopGroup.ServiceModes
            val sm2 = stopGroup.ServiceModes2
            val lc = stopGroup.LineCodes
            val stps = stopGroup.Stops

            if (ident != null && desc != null && loc != null && sm1 != null && sm2 != null) {
                return StopGroup(
                    identifier = ident,
                    description = desc,
                    location = loc,
                    serviceModes = sm1,
                    serviceModes2 = sm2,
                    lineCodes = lc,
                    stops = stps?.mapNotNull { s -> mapStop(s) }
                )
            }
            return null
        }

        fun mapPassingTimeResponse(passingTime: PassingTimeResponse): PassingTime? {
            val ts = passingTime.Timestamp
            val tid = passingTime.TripIdentifier
            val st = passingTime.Status
            val dt = passingTime.DisplayTime
            val nt = passingTime.Notes
            val pi = passingTime.PredictionInaccurate
            val psd = passingTime.Passed

            if (ts != null && tid != null && st != null && dt != null && nt != null && pi != null && psd != null) {
                return PassingTime(
                    timestamp = ts,
                    tripIdentifier = tid,
                    status = st,
                    displayTime = dt,
                    notes = nt,
                    predictionInaccurate = pi,
                    passed = psd
                )
            }
            return null
        }

        fun mapRouteDirectionResponse(routeDirection: RouteDirectionResponse): RouteDirection? {
            val pid: String? = routeDirection.PublicIdentifier
            val dir: String? = routeDirection.Direction
            val dirn: String? = routeDirection.DirectionName
            val sm: String? = routeDirection.ServiceMode
            val sm2: String? = routeDirection.ServiceMode2
            val id: String? = routeDirection.Identifier
            val pt: List<PassingTimeResponse>? = routeDirection.PassingTimes
            val nts: List<Any>? = routeDirection.Notes


            return RouteDirection(
                publicIdentifier = pid,
                direction = dir,
                directionName = dirn,
                serviceMode = sm,
                serviceMode2 = sm2,
                identifier = id,
                passingTimes = pt?.let { p -> mapAllPassingTimeResponses(p).filterNotNull() },
                notes = nts
            )
        }

        fun mapStop(stop: StopResponse): Stop {
            return Stop(
                identifier = stop.Identifier ?: "",
                description = stop.Description,
                location = stop.Location,
                serviceModes = stop.ServiceModes,
                serviceModes2 = stop.ServiceModes2,
                detail = stop.Detail,
                skyssId = stop.SkyssId,
                routeDirections = stop.RouteDirections?.mapNotNull { r ->
                    mapRouteDirectionResponse(
                        r
                    )
                },
                platform = stop.Platform
            )
        }

        fun mapTimeTable(timeTable: TimeTableResponse): TimeTable? {
            val pt: List<PassingTimeResponse>? = timeTable.PassingTimes
            if (pt != null) {
                return TimeTable(
                    passingTimes = mapAllPassingTimeResponses(pt).filterNotNull(),
                )
            }
            return null
        }

        fun mapAllTimeTables(timeTables: List<TimeTableResponse>): List<TimeTable?> {
            return timeTables.map { tt -> mapTimeTable(tt) }
        }

        fun mapAllStopGroupResponses(stopGroups: List<StopGroupResponse>): List<StopGroup?> {
            return stopGroups.map { stop -> mapStopGroupResponse(stop) }
        }

        fun mapAllPassingTimeResponses(passingTimes: List<PassingTimeResponse>): List<PassingTime?> {
            return passingTimes.map { passingTime -> mapPassingTimeResponse(passingTime) }
        }
    }
}