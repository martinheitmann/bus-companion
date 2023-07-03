package com.app.skyss_companion.sampledata

import com.app.skyss_companion.model.RouteDirection
import com.app.skyss_companion.model.Stop
import com.app.skyss_companion.model.travelplanner.End
import com.app.skyss_companion.model.travelplanner.Intermediate
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.model.travelplanner.TravelStep
import java.time.ZoneId
import java.time.ZonedDateTime

class TravelPlanListSample {

    val travelPlanSample: List<TravelPlan> = listOf(
        TravelPlan(
            id = "r-d05ed92a-b9a7-448d-bc8e-29bc3a037174",
            end = End(
                description = "Festplassen",
                identifier = "NSR:Quay:53203",
                location = "60.391045,5.325373",
                platform = "M",
            ),
            endTime = ZonedDateTime.of(2023, 4, 23, 14, 9, 0, 0, ZoneId.of("UTC")),
            startTime = ZonedDateTime.of(2023, 4, 23, 13, 57, 0, 0, ZoneId.of("UTC")),
            travelSteps = listOf(
                TravelStep(
                    callIdentifier = "NSR:Quay:55006_SKY:ServiceJourney:3-167522-13501200_23042023-1357_stopNSR:Quay:53203_dt2023-04-23_",
                    distance = "7748",
                    duration = "PT12M",
                    endTime = ZonedDateTime.of(2023, 4, 23, 14, 9, 0, 0, ZoneId.of("UTC")),
                    expectedEndTime = ZonedDateTime.of(2023, 4, 23, 14, 9, 0, 0, ZoneId.of("UTC")),
                    intermediates = listOf(
                        Intermediate(
                            aimedTime = "13:58",
                            status = "Schedule",
                            stopName = "Bjørndalsbakken"
                        ),
                        Intermediate(
                            aimedTime = "13:59",
                            status = "Schedule",
                            stopName = "Tennebekk"
                        ),
                        Intermediate(
                            aimedTime = "14:03",
                            status = "Schedule",
                            stopName = "Lyngbø rv. 555"
                        ),
                        Intermediate(
                            aimedTime = "14:08",
                            status = "Schedule",
                            stopName = "Møhlenpris"
                        )
                    ),
                    notes = emptyList(),
                    passed = false,
                    path = "",
                    routeDirection = RouteDirection(
                        directionName = "Til Støbotn",
                        identifier = "SKY:Line:3~outbound",
                        notes = emptyList(),
                        passingTimes = emptyList(),
                        publicIdentifier = "3",
                        serviceMode = "Bus",
                        serviceMode2 = "Bus"
                    ),
                    routeDirectionIdentifier = "SKY:Line:3~outbound",
                    startTime = ZonedDateTime.of(2023, 4, 23, 13, 57, 0, 0, ZoneId.of("UTC")),
                    status = "Schedule",
                    stop = Stop(
                        description = "Loddefjord terminal",
                        identifier = "NSR:Quay:55006",
                        location = "60.361744,5.235398",
                        platform = "A",
                        routeDirections = listOf(
                            RouteDirection(
                                directionName = "Til Støbotn",
                                identifier = "SKY:Line:3~outbound",
                                notes = emptyList(),
                                passingTimes = emptyList(),
                                publicIdentifier = "3",
                                serviceMode = "Bus",
                                serviceMode2 = "Bus"
                            )
                        ),
                        serviceModes = listOf("bus")
                    ),
                    stopIdentifier = "NSR:Quay:55006",
                    tripIdentifier = "SKY:ServiceJourney:3-167522-13501200",
                    type = "route"
                ),
            ),
            url = "https://reise.skyss.no/planner/travel-plan/r-d05ed92a-b9a7-448d-bc8e-29bc3a037174"
        ),
        TravelPlan(
            id = "r-16d5aee3-21bc-4ae7-8324-f58280ce7b0a",
            end = End(
                description = "Festplassen",
                identifier = "NSR:Quay:53203",
                location = "60.391045,5.325373",
                platform = "M"
            ),
            endTime = ZonedDateTime.of(2023, 4, 23, 14, 29, 0, 0, ZoneId.of("UTC")),
            startTime = ZonedDateTime.of(2023, 4, 23, 14, 17, 0, 0, ZoneId.of("UTC")),
            travelSteps = listOf(
                TravelStep(
                    callIdentifier = "NSR:Quay:55006_SKY:ServiceJourney:3-167522-13501201_23042023-1417_stopNSR:Quay:53203_dt2023-04-23_",
                    distance = "7748",
                    duration = "PT12M",
                    endTime = ZonedDateTime.of(2023, 4, 23, 14, 29, 0, 0, ZoneId.of("UTC")),
                    expectedEndTime = ZonedDateTime.of(2023, 4, 23, 14, 29, 0, 0, ZoneId.of("UTC")),
                    intermediates = listOf(
                        Intermediate(
                            aimedTime = "14:18",
                            status = "Schedule",
                            stopName = "Bjørndalsbakken"
                        ),
                        Intermediate(
                            aimedTime = "14:19",
                            status = "Schedule",
                            stopName = "Tennebekk"
                        ),
                        Intermediate(
                            aimedTime = "14:23",
                            status = "Schedule",
                            stopName = "Lyngbø rv. 555"
                        ),
                        Intermediate(
                            aimedTime = "14:28",
                            status = "Schedule",
                            stopName = "Møhlenpris"
                        )
                    ),
                    notes = emptyList(),
                    passed = false,
                    path = "",
                    routeDirection = RouteDirection(
                        directionName = "Til Støbotn",
                        identifier = "SKY:Line:3~outbound",
                        notes = emptyList(),
                        passingTimes = emptyList(),
                        publicIdentifier = "3",
                        serviceMode = "Bus",
                        serviceMode2 = "Bus"
                    ),
                    routeDirectionIdentifier = "SKY:Line:3~outbound",
                    startTime = ZonedDateTime.of(2023, 4, 23, 14, 17, 0, 0, ZoneId.of("UTC")),
                    status = "Schedule",
                    stop = Stop(
                        description = "Loddefjord terminal",
                        identifier = "NSR:Quay:55006",
                        location = "60.361744,5.235398",
                        platform = "A",
                        routeDirections = listOf(
                            RouteDirection(
                                directionName = "Til Støbotn",
                                identifier = "SKY:Line:3~outbound",
                                notes = emptyList(),
                                passingTimes = emptyList(),
                                publicIdentifier = "3",
                                serviceMode = "Bus",
                                serviceMode2 = "Bus"
                            )
                        ),
                        serviceModes = listOf("bus")
                    ),
                    stopIdentifier = "NSR:Quay:55006",
                    tripIdentifier = "SKY:ServiceJourney:3-167522-13501201",
                    type = "route"
                )
            ),
            url = "https://reise.skyss.no/planner/travel-plan/r-16d5aee3-21bc-4ae7-8324-f58280ce7b0a"
        ),
        TravelPlan(
            id = "r-2e4f631b-7bc2-4a1c-bb29-5ddabe54b048",
            end = End(
                description = "Festplassen",
                identifier = "NSR:Quay:53204",
                location = "60.39163,5.325974",
                platform = "N"
            ),
            endTime = ZonedDateTime.of(2023, 4, 23, 14, 47, 0, 0, ZoneId.of("UTC")),
            startTime = ZonedDateTime.of(2023, 4, 23, 14, 21, 0, 0, ZoneId.of("UTC")),
            travelSteps = listOf(
                TravelStep(
                    callIdentifier = "NSR:Quay:55006_SKY:ServiceJourney:20-169416-14612576_23042023-1421_stopNSR:Quay:54842_dt2023-04-23_",
                    distance = "6537",
                    duration = "PT12M",
                    endTime = ZonedDateTime.of(2023, 4, 23, 14, 33, 0, 0, ZoneId.of("UTC")),
                    expectedEndTime = ZonedDateTime.of(2023, 4, 23, 14, 33, 0, 0, ZoneId.of("UTC")),
                    intermediates = listOf(
                        Intermediate(
                            aimedTime = "14:22",
                            status = "Schedule",
                            stopName = "Bjørndalsbakken"
                        ),
                        Intermediate(
                            aimedTime = "14:23",
                            status = "Schedule",
                            stopName = "Tennebekk"
                        ),
                        Intermediate(
                            aimedTime = "14:27",
                            status = "Schedule",
                            stopName = "Lyngbø"
                        ),
                        Intermediate(
                            aimedTime = "14:28",
                            status = "Schedule",
                            stopName = "Lyderhornsveien"
                        ),
                        Intermediate(
                            aimedTime = "14:29",
                            status = "Schedule",
                            stopName = "Nygård kirke"
                        ),
                        Intermediate(
                            aimedTime = "14:30",
                            status = "Schedule",
                            stopName = "Kringsjå"
                        ),
                        Intermediate(
                            aimedTime = "14:30",
                            status = "Schedule",
                            stopName = "Holen"
                        ),
                        Intermediate(
                            aimedTime = "14:31",
                            status = "Schedule",
                            stopName = "Laksevåg senter"
                        ),
                        Intermediate(
                            aimedTime = "14:32",
                            status = "Schedule",
                            stopName = "Laksevåg"
                        ),

                        ),
                    notes = emptyList(),
                    passed = false,
                    path = "",
                    routeDirection = RouteDirection(
                        directionName = "Til Nesttun terminal",
                        identifier = "SKY:Line:20~inbound",
                        notes = emptyList(),
                        passingTimes = emptyList(),
                        publicIdentifier = "20",
                        serviceMode = "Bus",
                        serviceMode2 = "Bus"
                    ),
                    routeDirectionIdentifier = "SKY:Line:20~inbound",
                    startTime = ZonedDateTime.of(2023, 4, 23, 14, 21, 0, 0, ZoneId.of("UTC")),
                    status = "Schedule",
                    stop = Stop(
                        description = "Loddefjord terminal",
                        identifier = "NSR:Quay:55006",
                        location = "60.361744,5.235398",
                        platform = "A",
                        routeDirections = listOf(
                            RouteDirection(
                                directionName = "Til Nesttun terminal",
                                identifier = "SKY:Line:20~inbound",
                                notes = emptyList(),
                                passingTimes = emptyList(),
                                publicIdentifier = "20",
                                serviceMode = "Bus",
                                serviceMode2 = "Bus"
                            )
                        ),
                        serviceModes = listOf("bus")
                    ),
                    stopIdentifier = "NSR:Quay:55006",
                    tripIdentifier = "SKY:ServiceJourney:20-169416-14612576",
                    type = "route"
                ),
                TravelStep(
                    callIdentifier = "NSR:Quay:54842_SKY:ServiceJourney:6-169416-13725129_23042023-1441_stopNSR:Quay:53204_dt2023-04-23_",
                    distance = "2487",
                    duration = "PT6M",
                    endTime = ZonedDateTime.of(2023, 4, 23, 14, 47, 0, 0, ZoneId.of("UTC")),
                    expectedEndTime = ZonedDateTime.of(2023, 4, 23, 14, 47, 0, 0, ZoneId.of("UTC")),
                    intermediates = listOf(
                        Intermediate(
                            aimedTime = "14:42",
                            status = "Schedule",
                            stopName = "Carl Konows gate"
                        ),
                        Intermediate(
                            aimedTime = "14:43",
                            status = "Schedule",
                            stopName = "Gyldenpris N"
                        ),
                        Intermediate(
                            aimedTime = "14:45",
                            status = "Schedule",
                            stopName = "Møhlenpris"
                        ),
                    ),
                    notes = emptyList(),
                    passed = false,
                    path = "",
                    routeDirection = RouteDirection(
                        directionName = "Til Birkelundstoppen",
                        identifier = "SKY:Line:6~outbound",
                        notes = emptyList(),
                        passingTimes = emptyList(),
                        publicIdentifier = "6",
                        serviceMode = "Bus",
                        serviceMode2 = "Bus"
                    ),
                    routeDirectionIdentifier = "SKY:Line:6~outbound",
                    startTime = ZonedDateTime.of(2023, 4, 23, 14, 41, 0, 0, ZoneId.of("UTC")),
                    status = "Schedule",
                    stop = Stop(
                        description = "Damsgård hovedgård",
                        identifier = "NSR:Quay:54842",
                        location = "60.384117,5.300813",
                        routeDirections = listOf(
                            RouteDirection(
                                directionName = "Til Birkelundstoppen",
                                identifier = "SKY:Line:6~outbound",
                                notes = emptyList(),
                                passingTimes = emptyList(),
                                publicIdentifier = "6",
                                serviceMode = "Bus",
                                serviceMode2 = "Bus"
                            )
                        ),
                        serviceModes = listOf("bus")
                    ),
                    stopIdentifier = "NSR:Quay:54842",
                    tripIdentifier = "SKY:ServiceJourney:6-169416-13725129",
                    type = "route"
                )
            ),
            url = "https://reise.skyss.no/planner/travel-plan/r-16d5aee3-21bc-4ae7-8324-f58280ce7b0a"
        ),
        TravelPlan(
            id = "r-1f3cd8de-a00c-4ceb-90d8-7e10ebe70da0",
            end = End(
                description = "Festplassen",
                identifier = "NSR:Quay:53203",
                location = "60.391045,5.325373",
                platform = "M",
            ),
            endTime = ZonedDateTime.of(2023, 4, 23, 14, 49, 0, 0, ZoneId.of("UTC")),
            startTime = ZonedDateTime.of(2023, 4, 23, 13, 37, 0, 0, ZoneId.of("UTC")),
            travelSteps = listOf(
                TravelStep(
                    callIdentifier = "NSR:Quay:55006_SKY:ServiceJourney:3-167522-13501202_23042023-1437_stopNSR:Quay:53203_dt2023-04-23_",
                    distance = "7748",
                    duration = "PT12M",
                    endTime = ZonedDateTime.of(2023, 4, 23, 14, 49, 0, 0, ZoneId.of("UTC")),
                    expectedEndTime = ZonedDateTime.of(2023, 4, 23, 14, 49, 0, 0, ZoneId.of("UTC")),
                    intermediates = listOf(
                        Intermediate(
                            aimedTime = "14:38",
                            status = "Schedule",
                            stopName = "Bjørndalsbakken"
                        ),
                        Intermediate(
                            aimedTime = "14:39",
                            status = "Schedule",
                            stopName = "Tennebekk"
                        ),
                        Intermediate(
                            aimedTime = "14:43",
                            status = "Schedule",
                            stopName = "Lyngbø rv. 555"
                        ),
                        Intermediate(
                            aimedTime = "14:48",
                            status = "Schedule",
                            stopName = "Møhlenpris"
                        )
                    ),
                    notes = emptyList(),
                    passed = false,
                    path = "",
                    routeDirection = RouteDirection(
                        directionName = "Til Støbotn",
                        identifier = "SKY:Line:3~outbound",
                        notes = emptyList(),
                        passingTimes = emptyList(),
                        publicIdentifier = "3",
                        serviceMode = "Bus",
                        serviceMode2 = "Bus"
                    ),
                    routeDirectionIdentifier = "SKY:Line:3~outbound",
                    startTime = ZonedDateTime.of(2023, 4, 23, 14, 37, 0, 0, ZoneId.of("UTC")),
                    status = "Schedule",
                    stop = Stop(
                        description = "Loddefjord terminal",
                        identifier = "NSR:Quay:55006",
                        location = "60.361744,5.235398",
                        platform = "A",
                        routeDirections = listOf(
                            RouteDirection(
                                directionName = "Til Støbotn",
                                identifier = "SKY:Line:3~outbound",
                                notes = emptyList(),
                                passingTimes = emptyList(),
                                publicIdentifier = "3",
                                serviceMode = "Bus",
                                serviceMode2 = "Bus"
                            )
                        ),
                        serviceModes = listOf("bus")
                    ),
                    stopIdentifier = "NSR:Quay:55006",
                    tripIdentifier = "SKY:ServiceJourney:3-167522-13501202",
                    type = "route"
                ),
            ),
            url = "https://reise.skyss.no/planner/travel-plan/r-1f3cd8de-a00c-4ceb-90d8-7e10ebe70da0"
        ),
        TravelPlan(
            id = "r-3fa65420-6c06-451a-8f60-0f9ff38ba619",
            end = End(
                description = "Festplassen",
                identifier = "NSR:Quay:53203",
                location = "60.391045,5.325373",
                platform = "M",
            ),
            endTime = ZonedDateTime.of(2023, 4, 23, 15, 9, 0, 0, ZoneId.of("UTC")),
            startTime = ZonedDateTime.of(2023, 4, 23, 14, 57, 0, 0, ZoneId.of("UTC")),
            travelSteps = listOf(
                TravelStep(
                    callIdentifier = "NSR:Quay:55006_SKY:ServiceJourney:3-167522-13501203_23042023-1457_stopNSR:Quay:53203_dt2023-04-23_",
                    distance = "7748",
                    duration = "PT12M",
                    endTime = ZonedDateTime.of(2023, 4, 23, 15, 9, 0, 0, ZoneId.of("UTC")),
                    expectedEndTime = ZonedDateTime.of(2023, 4, 23, 15, 9, 0, 0, ZoneId.of("UTC")),
                    intermediates = listOf(
                        Intermediate(
                            aimedTime = "14:58",
                            status = "Schedule",
                            stopName = "Bjørndalsbakken"
                        ),
                        Intermediate(
                            aimedTime = "14:59",
                            status = "Schedule",
                            stopName = "Tennebekk"
                        ),
                        Intermediate(
                            aimedTime = "15:03",
                            status = "Schedule",
                            stopName = "Lyngbø rv. 555"
                        ),
                        Intermediate(
                            aimedTime = "15:08",
                            status = "Schedule",
                            stopName = "Møhlenpris"
                        )
                    ),
                    notes = emptyList(),
                    passed = false,
                    path = "",
                    routeDirection = RouteDirection(
                        directionName = "Til Støbotn",
                        identifier = "SKY:Line:3~outbound",
                        notes = emptyList(),
                        passingTimes = emptyList(),
                        publicIdentifier = "3",
                        serviceMode = "Bus",
                        serviceMode2 = "Bus"
                    ),
                    routeDirectionIdentifier = "SKY:Line:3~outbound",
                    startTime = ZonedDateTime.of(2023, 4, 23, 14, 57, 0, 0, ZoneId.of("UTC")),
                    status = "Schedule",
                    stop = Stop(
                        description = "Loddefjord terminal",
                        identifier = "NSR:Quay:55006",
                        location = "60.361744,5.235398",
                        platform = "A",
                        routeDirections = listOf(
                            RouteDirection(
                                directionName = "Til Støbotn",
                                identifier = "SKY:Line:3~outbound",
                                notes = emptyList(),
                                passingTimes = emptyList(),
                                publicIdentifier = "3",
                                serviceMode = "Bus",
                                serviceMode2 = "Bus"
                            )
                        ),
                        serviceModes = listOf("bus")
                    ),
                    stopIdentifier = "NSR:Quay:55006",
                    tripIdentifier = "SKY:ServiceJourney:3-167522-13501203",
                    type = "route"
                ),
            ),
            url = "https://reise.skyss.no/planner/travel-plan/r-3fa65420-6c06-451a-8f60-0f9ff38ba619"
        )
    )
}