package com.app.skyss_companion.mappers

import com.app.skyss_companion.http.model.travelplanner.*
import com.app.skyss_companion.misc.DateUtils
import com.app.skyss_companion.model.RouteDirection
import com.app.skyss_companion.model.Stop
import com.app.skyss_companion.model.travelplanner.*
import java.time.OffsetDateTime

class TravelPlannerEntityMapper {
    companion object {

        fun mapApiTravelPlannerResponse(apiTravelPlannerResponse: ApiTravelPlannerResponse): TravelPlannerRoot {
            val resultCode: String? = apiTravelPlannerResponse.resultCode
            val travelPlans: List<TravelPlan> =
                apiTravelPlannerResponse.TravelPlans?.mapNotNull { t -> mapTravelPlanResponse(t) }
                    ?: emptyList()
            return TravelPlannerRoot(resultCode = resultCode, travelPlans = travelPlans)
        }

        private fun mapTravelPlanResponse(travelPlanResponse: TravelPlanResponse?): TravelPlan? {
            if (travelPlanResponse == null) return null
            val id: String? = travelPlanResponse.id
            val url: String? = travelPlanResponse.url
            val startTime: OffsetDateTime? =
                DateUtils.parseOffsetDateTime(travelPlanResponse.StartTime)
            val endTime: OffsetDateTime? = DateUtils.parseOffsetDateTime(travelPlanResponse.EndTime)
            val end: End? = mapEndResponse(travelPlanResponse.End)
            val travelSteps: List<TravelStep> =
                travelPlanResponse.TravelSteps?.mapNotNull { t -> mapTravelStepResponse(t) }
                    ?: emptyList()
            return TravelPlan(
                id = id,
                url = url,
                startTime = startTime,
                endTime = endTime,
                end = end,
                travelSteps = travelSteps
            )
        }

        private fun mapTravelStepResponse(travelStepResponse: TravelStepResponse?): TravelStep? {
            if (travelStepResponse == null) return null
            val type: String? = travelStepResponse.Type
            val startTime: OffsetDateTime? =
                DateUtils.parseOffsetDateTime(travelStepResponse.StartTime)
            val endTime: OffsetDateTime? = DateUtils.parseOffsetDateTime(travelStepResponse.EndTime)
            val distance: String? = travelStepResponse.Distance
            val stopIdentifier: String? = travelStepResponse.StopIdentifier
            val duration: String? = travelStepResponse.Duration
            val status: String? = travelStepResponse.Status
            val path: String? = travelStepResponse.Path
            val tripIdentifier: String? = travelStepResponse.TripIdentifier
            val callIdentifier: String? = travelStepResponse.CallIdentifier
            val routeDirectionIdentifier: String? = travelStepResponse.RouteDirectionIdentifier
            val routeDirection: RouteDirection? = if (travelStepResponse.RouteDirection != null) {
                StopResponseEntityMapper.mapRouteDirectionResponse(travelStepResponse.RouteDirection)
            } else null
            val stop: Stop? = travelStepResponse.Stop?.let { StopResponseEntityMapper.mapStop(it) }
            val notes: List<Any> = travelStepResponse.Notes ?: emptyList()
            val expectedEndTime: OffsetDateTime? =
                DateUtils.parseOffsetDateTime(travelStepResponse.ExpectedEndTime)
            val intermediates: List<Intermediate> =
                travelStepResponse.Intermediates?.mapNotNull { i -> mapIntermediateResponse(i) }
                    ?: emptyList()
            val passed: Boolean = travelStepResponse.Passed
            val occupancy: String? = travelStepResponse.Occupancy
            val displayTime: String? = travelStepResponse.DisplayTime

            return TravelStep(
                type = type,
                startTime = startTime,
                endTime = endTime,
                distance = distance,
                stopIdentifier = stopIdentifier,
                duration = duration,
                status = status,
                path = path,
                tripIdentifier = tripIdentifier,
                callIdentifier = callIdentifier,
                routeDirectionIdentifier = routeDirectionIdentifier,
                routeDirection = routeDirection,
                stop = stop,
                notes = notes,
                expectedEndTime = expectedEndTime,
                intermediates = intermediates,
                passed = passed,
                occupancy = occupancy,
                displayTime = displayTime
            )
        }

        private fun mapIntermediateResponse(intermediateResponse: IntermediateResponse?): Intermediate? {
            if (intermediateResponse == null) return null
            val stopName: String? = intermediateResponse.StopName
            val status: String? = intermediateResponse.Status
            val aimedTime: String? = intermediateResponse.AimedTime
            return Intermediate(stopName = stopName, status = status, aimedTime = aimedTime)
        }

        private fun mapEndResponse(endResponse: EndResponse?): End? {
            if (endResponse == null) return null
            val description: String? = endResponse.Description
            val location: String? = endResponse.Location
            val platform: String? = endResponse.Platform
            val identifier: String? = endResponse.Identifier
            return End(
                description = description,
                location = location,
                identifier = identifier,
                platform = platform
            )
        }

    }
}