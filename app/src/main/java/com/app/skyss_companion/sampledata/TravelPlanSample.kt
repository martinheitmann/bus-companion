package com.app.skyss_companion.sampledata

import com.app.skyss_companion.model.RouteDirection
import com.app.skyss_companion.model.Stop
import com.app.skyss_companion.model.travelplanner.End
import com.app.skyss_companion.model.travelplanner.Intermediate
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.model.travelplanner.TravelStep
import java.time.ZoneId
import java.time.ZonedDateTime

class TravelPlanSample {

    val travelPlanSample: TravelPlan = TravelPlan(
        id = "r-1b832025-468e-41b7-8f11-e3f0e0d50422",
        url = "https://reise.skyss.no/planner/travel-plan/r-1b832025-468e-41b7-8f11-e3f0e0d50422",
        startTime = ZonedDateTime.of(2023, 6, 18, 13, 47, 0, 0, ZoneId.of("UTC")),
        endTime = ZonedDateTime.of(2023, 6, 18, 14, 36, 0, 0, ZoneId.of("UTC")),
        end = End(
            description = "Melkeplassen, Bergen",
            location = "60.376186,5.3064337"
        ),
        travelSteps = listOf(
            TravelStep(
                type = "walk",
                startTime = ZonedDateTime.of(2023, 6, 18, 13, 47, 32, 0, ZoneId.of("UTC")),
                endTime = ZonedDateTime.of(2023, 6, 18, 13, 53, 0, 0, ZoneId.of("UTC")),
                distance = "351",
                duration = "PT5M",
                status = "Schedule",
                path = "_vloJue{^BGVcAFY?k@@cAAO?e@\\kANg@Ha@De@?uBAaD?qAAWH?H??UCQAMEKCOPSHKZ_@p@w@BN",
                stop = Stop(
                    identifier = "",
                    description = "Vestre Vadmyra, Bergen",
                    location = "60.363438,5.223603",
                    serviceModes = emptyList()
                ),
                notes = emptyList(),
                expectedEndTime = ZonedDateTime.of(2023, 6, 18, 13, 53, 0, 0, ZoneId.of("UTC")),
                passed = false,
                intermediates = emptyList()
            ),
            TravelStep(
                type = "route",
                startTime = ZonedDateTime.of(2023, 6, 18, 13, 53, 32, 0, ZoneId.of("UTC")),
                endTime = ZonedDateTime.of(2023, 6, 18, 14, 8, 0, 0, ZoneId.of("UTC")),
                distance = "8064",
                stopIdentifier = "NSR:Quay:55034",
                duration = "PT5M",
                status = "OnTime",
                path = "soloJif|^@Al@u@b@kARc@x@aC`@iANSVc@l@y@X_@d@a@FGbAq@XKLA`@SFC^c@??@?LUXm@Tk@Ji@BQJg@BW@WHkABO@K?SAKEOIIGAGBEBELKNUN]NO?wA?S?UCKESe@K]G_@@KAKAKCICGEEEACMGWSo@Ci@Gc@EWEYEMKMGMKU?KAICKEKEGECEAG?GDEFEHCLAJ?P@PBLDLDFNfAAn@i@IO^e@r@SZGJEFIHI@MY?WH[FIR[f@s@??JODEHEHBDJ@LATh@H@o@HeADEDIBMBO?Q?KAIH]HSNYRWVUVQXQVK`@Ml@Qp@S^KTMXORMNOPUN[L]L]H_@Jc@F_@Fg@Dy@Bm@?e@Ak@Ck@Ek@Gk@Ie@I_@Me@KYQa@Qa@??AAOYYa@SYUYYWq@g@{@s@WQYUKKMIIIi@_@k@i@u@s@_@_@Y]Wa@m@}@c@w@SWUc@c@cAa@_A]s@]s@[e@]i@[a@OQ??SWc@e@g@g@e@_@a@Yg@[y@c@s@_@c@UYKQGSOKIOMGGAMCKCIEGECECESEWC[AYAS@U@YBWDUFYHa@`@{AH]De@B_@@_@@c@A[CWE[Ic@GWGQIMGMSUKGMEKCM?UFe@NYFe@Hc@Dc@Fc@@c@?e@A{@Iu@Ea@Ca@?q@D[DaAPaBLWT_GTq@Ai@A_@Ci@Ki@Oo@[]Wc@_@s@s@kAoA{EmFaC}CsBuCkCgEgAmBw@wA_CmEiAuBc@y@_@w@]u@Ws@Qg@WgAUiASmAMoAIqAGuAIkDEoBIcBIeAMsAGqAUmAQw@GWEYEYIa@[{@EQOe@M[QQIGAA??GEKEGCWg@g@aAc@_Aa@mAUk@UwAM}@KaAIkAEaAImCAqC?mEBwE`C_gBlAc|@LcJFaH@wGCqFEoBIeCMiCMkBMaBUyBQ_B[uBe@iCc@oB_@}AOa@Sk@Um@Q]Yi@g@{@]o@sOmVMS]qAWiAGWM[OSQEQFGFGFGF",
                tripIdentifier = "SKY:ServiceJourney:3-169514-13501206",
                callIdentifier = "NSR:Quay:55034_SKY:ServiceJourney:3-169514-13501206_18062023-1553_stopNSR:Quay:53230_dt2023-06-18_",
                routeDirectionIdentifier = "SKY:Line:3~outbound",
                routeDirection = RouteDirection(
                    publicIdentifier = "3",
                    directionName = "Til Støbotn",
                    serviceMode = "Bus",
                    serviceMode2 = "Bus",
                    identifier = "SKY:Line:3~outbound",
                    passingTimes = emptyList(),
                    notes = emptyList()
                ),
                stop = Stop(
                    identifier = "NSR:Quay:55034",
                    description = "Vestre Vadmyra",
                    location = "60.362324,5.228613",
                    serviceModes = listOf("bus"),
                    routeDirections = listOf(
                        RouteDirection(
                            publicIdentifier = "3",
                            directionName = "Til Støbotn",
                            serviceMode = "Bus",
                            serviceMode2 = "Bus",
                            identifier = "SKY:Line:3~outbound",
                            passingTimes = emptyList(),
                            notes = emptyList()
                        )
                    )
                ),
                displayTime = "14 min",
                notes = emptyList(),
                expectedEndTime = ZonedDateTime.of(2023, 6, 18, 14, 8, 0, 0, ZoneId.of("UTC")),
                intermediates = listOf(
                    Intermediate(
                        stopName = "Lyngfaret",
                        status = "Schedule",
                        aimedTime = "15:55"
                    ),
                    Intermediate(
                        stopName = "Loddefjord terminal",
                        status = "Schedule",
                        aimedTime = "15:57"
                    ),
                    Intermediate(
                        stopName = "Bjørndalsbakken",
                        status = "Schedule",
                        aimedTime = "15:58"
                    ),
                    Intermediate(
                        stopName = "Tennebekk",
                        status = "Schedule",
                        aimedTime = "15:59"
                    ),
                    Intermediate(
                        stopName = "Lyngbø rv. 555",
                        status = "Schedule",
                        aimedTime = "16:03"
                    )
                ),
                passed = false,
            ),
            TravelStep(
                type = "route",
                startTime = ZonedDateTime.of(2023, 6, 18, 14, 26, 0, 0, ZoneId.of("UTC")),
                endTime = ZonedDateTime.of(2023, 6, 18, 14, 33, 0, 0, ZoneId.of("UTC")),
                distance = "2221",
                stopIdentifier = "NSR:Quay:53228",
                duration = "PT7M",
                status = "Schedule",
                path = "sbqoJc~m_@@BZRLTtP~WXl@LTRl@Hx@B^@`@Ab@Cj@Iz@Ir@CPW|B??CTGZKTEHE@U?OAG?GBIx@AJIz@E`@En@WfEG`AY|BSbAK^St@a@vAOb@eAnDUz@IZ?@??I^G`@CZCZ?h@?\\Bp@B\\N|AHn@DA@AHAL?X@PDRDPJTNPNLHFFHDHBHBLDHBHBNDB@D@HBNFTFLDNDPBF?H?N?JAJ?P?LARMPOLQHQFM??@EDOL[DOBEDGDEDCD?DBDFDHBHFp@D\\Nd@HPRb@JTTn@DLXhADVFb@@P?V?T?T@T@TDNFHLADCXc@JQ??JMX_@rA}Av@_ANQLOHKHKJIDEZYPOZU^SPMb@Y??HGRM`BgADCRKRIRGRCPA\\BPD`@Jb@JNF^Lp@N",
                tripIdentifier = "SKY:ServiceJourney:19-169514-15107732",
                callIdentifier = "NSR:Quay:53228_SKY:ServiceJourney:19-169514-15107732_18062023-1626_stopNSR:Quay:54940_dt2023-06-18_",
                routeDirectionIdentifier = "SKY:Line:3~outbound",
                waitDuration = "PT16M",
                routeDirection = RouteDirection(
                    publicIdentifier = "19",
                    directionName = "Til Løvstakkskiftet",
                    serviceMode = "Bus",
                    serviceMode2 = "Bus",
                    identifier = "SKY:Line:19~inbound",
                    passingTimes = emptyList(),
                    notes = emptyList()
                ),
                stop = Stop(
                    identifier = "NSR:Quay:53228",
                    description = "Møhlenpris",
                    location = "60.385857,5.319424",
                    serviceModes = listOf("bus"),
                    routeDirections = listOf(
                        RouteDirection(
                            publicIdentifier = "19",
                            directionName = "Til Løvstakkskiftet",
                            serviceMode = "Bus",
                            serviceMode2 = "Bus",
                            identifier = "SKY:Line:19~inbound",
                            passingTimes = emptyList(),
                            notes = emptyList()
                        )
                    )
                ),
                notes = emptyList(),
                expectedEndTime = ZonedDateTime.of(2023, 6, 18, 14, 33, 0, 0, ZoneId.of("UTC")),
                intermediates = listOf(
                    Intermediate(
                        stopName = "Gyldenpris N",
                        status = "Schedule",
                        aimedTime = "16:27"
                    ),
                    Intermediate(
                        stopName = "Carl Konows gate",
                        status = "Schedule",
                        aimedTime = "16:29"
                    ),
                    Intermediate(
                        stopName = "Gabriel Tischendorfs vei",
                        status = "Schedule",
                        aimedTime = "16:30"
                    ),
                    Intermediate(
                        stopName = "Svingen",
                        status = "Schedule",
                        aimedTime = "16:31"
                    ),
                    Intermediate(
                        stopName = "Kaptein Amlands vei",
                        status = "Schedule",
                        aimedTime = "16:32"
                    )
                ),
                passed = false,
            ),
            TravelStep(
                type = "walk",
                startTime = ZonedDateTime.of(2023, 6, 18, 14, 33, 0, 0, ZoneId.of("UTC")),
                endTime = ZonedDateTime.of(2023, 6, 18, 14, 36, 20, 0, ZoneId.of("UTC")),
                distance = "182",
                stopIdentifier = "NSR:Quay:54940",
                duration = "PT3M",
                status = "Schedule",
                path = "ofooJ}|j_@?Hf@N@[RFDu@DeA?Q?CEg@Cy@Am@CqA?u@MAYH",
                stop = Stop(
                    identifier = "NSR:Quay:54940",
                    description = "Melkeplassen",
                    location = "60.376244,5.303995",
                    serviceModes = listOf("bus")
                ),
                notes = emptyList(),
                expectedEndTime = ZonedDateTime.of(2023, 6, 18, 14, 36, 20, 0, ZoneId.of("UTC")),
                passed = false,
                intermediates = emptyList()
            )
        )
    )
}