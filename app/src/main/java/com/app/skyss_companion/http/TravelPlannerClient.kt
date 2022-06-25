package com.app.skyss_companion.http

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.model.travelplanner.ApiTravelPlannerResponse
import com.app.skyss_companion.model.travelplanner.TravelPlannerRoot
import com.squareup.moshi.Moshi
import okhttp3.*
import okio.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TravelPlannerClient @Inject constructor(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient
) {
    val tag = "TravelPlannerClient"

    private val travelPlannerResponseAdapter = moshi.adapter(ApiTravelPlannerResponse::class.java)

    @WorkerThread
    suspend fun getTravelPlans(
        fromLocation: List<Float>,
        toLocation: List<Float>,
        fromName: String,
        toName: String,
        fromStopGroupId: String,
        toStopGroupId: String,
        timeType: String,
        timestamp: String,
        modes: List<String>,
        minimumTransferTime: Int,
        maximumWalkDistance: Int
    ): ApiTravelPlannerResponse? {
        val httpUrl =
            HttpUrl.Builder()
                .scheme("https")
                .host("skyss-reise.giantleap.no")
                .addPathSegment("v3")
                .addPathSegment("travelplans")
                .addQueryParameter("FromLocation", fromLocation.joinToString(","))
                .addQueryParameter("ToLocation", toLocation.joinToString(","))
                .addQueryParameter("FromName", fromName)
                .addQueryParameter("ToName", toName)
                .addQueryParameter("FromStopGroupID", fromStopGroupId)
                .addQueryParameter("ToStopGroupID", toStopGroupId)
                .addQueryParameter("TimeType", timeType)
                .addQueryParameter("TS", timestamp)
                .addQueryParameter("modes", modes.joinToString(","))
                .addQueryParameter("minimumTransferTime", minimumTransferTime.toString())
                .addQueryParameter("maximumWalkDistance", maximumWalkDistance.toString())
                .build()
                .toString()

        val request = Request.Builder()
            .url(httpUrl)
            .build()

        Log.d(tag, "Requesting url $request")

        return suspendCoroutine { continuation ->
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) continuation.resumeWithException(IOException("Unexpected code $response"))
                        val responseBody = response.body
                        if (responseBody != null) {
                            val apiTravelPlannerResponse =
                                travelPlannerResponseAdapter.fromJson(responseBody.source())
                            continuation.resume(apiTravelPlannerResponse)
                        } else continuation.resume(null)
                    }
                }
            })
        }
    }

    @WorkerThread
    suspend fun getTravelPlanById(
        id: String
    ): ApiTravelPlannerResponse? {
        val httpUrl =
            HttpUrl.Builder()
                .scheme("https")
                .host("skyss-reise.giantleap.no")
                .addPathSegment("v3")
                .addPathSegment("travelplans")
                .addPathSegment(id)
                .build()
                .toString()

        val request = Request.Builder()
            .url(httpUrl)
            .build()

        Log.d(tag, "Requesting url $request")

        return suspendCoroutine { continuation ->
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) continuation.resumeWithException(IOException("Unexpected code $response"))
                        val responseBody = response.body
                        if (responseBody != null) {
                            val apiTravelPlannerResponse =
                                travelPlannerResponseAdapter.fromJson(responseBody.source())
                            continuation.resume(apiTravelPlannerResponse)
                        } else continuation.resume(null)
                    }
                }
            })
        }
    }

    fun getTravelPlansString1(): ApiTravelPlannerResponse? {
        val str =
            "{\"resultCode\":\"SUCCESS\",\"TravelPlans\":[{\"id\":\"r-71243b2d-9fd5-4886-a0e9-4ba86bc59b25\",\"url\":\"https://reise.skyss.no/planner/travel-plan/r-71243b2d-9fd5-4886-a0e9-4ba86bc59b25\",\"StartTime\":\"2022-06-06T10:37:00.000Z\",\"EndTime\":\"2022-06-06T11:08:00.000Z\",\"End\":{\"Identifier\":\"NSR:Quay:54941\",\"Description\":\"Melkeplassen\",\"Location\":\"60.37585,5.30398\"},\"TravelSteps\":[{\"Type\":\"route\",\"StartTime\":\"2022-06-06T10:37:00.000Z\",\"EndTime\":\"2022-06-06T10:50:00.000Z\",\"Distance\":\"7184\",\"StopIdentifier\":\"NSR:Quay:55006\",\"Duration\":\"PT13M\",\"Status\":\"Late\",\"Path\":\"_lloJqp}^BCDEHEHBDJ@LATh@H@o@HeADEDIBMBO?Q?KAIH]HSNYRWVUVQXQVK`@Ml@Qp@S^KTMXORMNOPUN[L]L]H_@Jc@F_@Fg@Dy@Bm@?e@Ak@Ck@Ek@Gk@Ie@I_@Me@KYQa@Qa@AAIOEIYa@SYUYYWq@g@{@s@WQYUKKMIIIi@_@k@i@u@s@_@_@Y]Wa@m@}@c@w@SWUc@c@cAa@_A]s@]s@[e@]i@U[EEOQSWc@e@g@g@e@_@a@Yg@[y@c@s@_@c@UYKQGSOKIOMGGAMCKCIEGECECESEWC[AYAS@U@YBWDUFYHa@`@{AH]De@B_@@_@@c@A[CWE[Ic@GWGQIMGMSUKGMEKCM?UFe@NYFe@Hc@Dc@Fc@@c@?e@A{@Iu@Ea@Ca@?q@D[DaAPaBLWT_GTq@Ai@A_@Ci@Ki@Oo@[]Wc@_@s@s@kAoA{EmFaC}CsBuCkCgEgAmBw@wA_CmEiAuBc@y@_@w@]u@Ws@Qg@WgAUiASmAMoAIqAGuAIkDEoBIcBIeAMsAGqAUmAQw@GWEYEYIa@[{@EQOe@M[QQIGA??AGEKEGCWg@g@aAc@_Aa@mAUk@UwAM}@KaAIkAEaAImCAqC?mEBwE`C_gBlAc|@LcJFaH@wGCqFEoBIeCMiCMkBMaBUyBQ_B[uBe@iCc@oB_@}AOa@Sk@Um@Q]Yi@g@{@]o@sOmVMS]qAWiAGWM[OSQEQFC@CDGFGF?@MTG`@Gd@Cp@AdA@\\\\@j@LLH?TUHc@BYCe@I_@UkASwAK]KS{BeCsAy@c@[o@e@qE{EWYq@y@GGKMeBsBIKw@u@w@q@GEWWQSAAKK]_@Y[m@k@_@a@g@g@OOAAAAKKk@m@CGIGe@g@_Ay@OMKIAAGCMGKAOCCb@AZE\\\\UdBGd@YvBKx@KFC@M?ICWMa@WCASCOCaAg@]UA?\",\"TripIdentifier\":\"SKY:ServiceJourney:3-158472-12710575\",\"CallIdentifier\":\"NSR:Quay:55006_SKY:ServiceJourney:3-158472-12710575_06062022-1237_stopNSR:Quay:53071_dt2022-06-06_\",\"RouteDirectionIdentifier\":\"SKY:Line:3~outbound\",\"RouteDirection\":{\"PublicIdentifier\":\"3\",\"DirectionName\":\"Til Støbotn\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:3~outbound\",\"PassingTimes\":[],\"Notes\":[]},\"Stop\":{\"Identifier\":\"NSR:Quay:55006\",\"Description\":\"Loddefjord terminal\",\"Location\":\"60.361744,5.235398\",\"ServiceModes\":[\"bus\"],\"RouteDirections\":[{\"PublicIdentifier\":\"3\",\"DirectionName\":\"Til Støbotn\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:3~outbound\",\"PassingTimes\":[],\"Notes\":[]}],\"Platform\":\"A\"},\"DisplayTime\":\"4 min\",\"Notes\":[],\"ExpectedEndTime\":\"2022-06-06T10:50:00.000Z\",\"Occupancy\":\"LOW\",\"Intermediates\":[{\"StopName\":\"Bjørndalsbakken\",\"Status\":\"Schedule\",\"AimedTime\":\"12:38\"},{\"StopName\":\"Tennebekk\",\"Status\":\"Schedule\",\"AimedTime\":\"12:39\"},{\"StopName\":\"Lyngbø rv. 555\",\"Status\":\"Schedule\",\"AimedTime\":\"12:43\"},{\"StopName\":\"Møhlenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"12:48\"},{\"StopName\":\"Festplassen\",\"Status\":\"Schedule\",\"AimedTime\":\"12:49\"}],\"Passed\":false},{\"Type\":\"walk\",\"StartTime\":\"2022-06-06T10:50:00.000Z\",\"EndTime\":\"2022-06-06T10:51:42.000Z\",\"Distance\":\"132\",\"StopIdentifier\":\"NSR:Quay:53071\",\"Duration\":\"PT2M\",\"Status\":\"Schedule\",\"Path\":\"_zroJwdo_@AP??cAo@MG??UM??QG??U@GBKL??W`@??S~@????\",\"Stop\":{\"Identifier\":\"NSR:Quay:53071\",\"Description\":\"Torget\",\"Location\":\"60.394722,5.325722\",\"ServiceModes\":[\"bus\"],\"Platform\":\"B\"},\"Notes\":[],\"ExpectedEndTime\":\"2022-06-06T10:51:42.000Z\",\"Passed\":false},{\"Type\":\"route\",\"StartTime\":\"2022-06-06T10:54:00.000Z\",\"EndTime\":\"2022-06-06T11:08:00.000Z\",\"Distance\":\"5203\",\"StopIdentifier\":\"NSR:Quay:53069\",\"Duration\":\"PT14M\",\"Status\":\"Late\",\"Path\":\"y`soJuco_@DGT_@LWHGTAPBVNp@b@B@zA~@l@^VNJHf@\\\\B?\\\\RD@F?DABADIDKHa@d@qDLJDBz@n@j@d@PLHFh@b@h@d@FF|@~@TT`@`@\\\\b@XZ^^NPHJn@x@PTBB\\\\d@JLJL~AjBLLFFz@|@^b@dAjAfAt@T@TOXa@j@mB@In@d@b@ZrAx@zBdCJRJ\\\\RvATjAH^Bd@CXi@P?kA@c@BODMDKFEL@B@V^XZ??@BZRLTtP~WXl@LTRl@Pn@VbAJXJRBB?@@@DFJLJFJFRFL@R@RFNFHFLLh@f@LHhp@hn@dSdRlWvVbAbApAlA|@n@l@Xl@RTB^DPB|@FrAHzBJv@Hn@Pz@Z\\\\R\\\\Z@NBLDHFDGbAOjB_@O]SWWa@e@S]g@w@S_@SMOGgEa@sBS}@Ik@M[Ki@[k@i@e@m@e@s@c@k@OK][[_@k@}@k@u@_@][Oa@Km@I[M[QUOMK{BiBQ]o@mACEu@gAy@oAm@y@s@}@GIeBoBMMGGSQUQa@Wc@O??C?SEQA]BQDUJYRSRo@t@MJKFKD[FU@IAk@A??G?WBWAm@EGAK@YEYM}@U_Bg@}@a@UMi@WOIkAk@?A\",\"TripIdentifier\":\"SKY:ServiceJourney:19-158472-13331803\",\"CallIdentifier\":\"NSR:Quay:53069_SKY:ServiceJourney:19-158472-13331803_06062022-1254_stopNSR:Quay:54941_dt2022-06-06_\",\"WaitDuration\":\"PT4M\",\"RouteDirectionIdentifier\":\"SKY:Line:19~outbound\",\"RouteDirection\":{\"PublicIdentifier\":\"19\",\"DirectionName\":\"Til Melkeplassen\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:19~outbound\",\"PassingTimes\":[],\"Notes\":[]},\"Stop\":{\"Identifier\":\"NSR:Quay:53069\",\"Description\":\"Torget\",\"Location\":\"60.39577,5.325437\",\"ServiceModes\":[\"bus\"],\"RouteDirections\":[{\"PublicIdentifier\":\"19\",\"DirectionName\":\"Til Melkeplassen\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:19~outbound\",\"PassingTimes\":[],\"Notes\":[]}],\"Platform\":\"D\"},\"DisplayTime\":\"12:55\",\"Notes\":[],\"ExpectedEndTime\":\"2022-06-06T11:08:00.000Z\",\"Occupancy\":\"LOW\",\"Intermediates\":[{\"StopName\":\"Olav Kyrres gate\",\"Status\":\"Schedule\",\"AimedTime\":\"12:55\"},{\"StopName\":\"Møhlenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"12:58\"},{\"StopName\":\"Gyldenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"13:00\"},{\"StopName\":\"Løvstakktunnelen\",\"Status\":\"Schedule\",\"AimedTime\":\"13:04\"},{\"StopName\":\"Klauvsteinen\",\"Status\":\"Schedule\",\"AimedTime\":\"13:07\"},{\"StopName\":\"Hamrehaugen\",\"Status\":\"Schedule\",\"AimedTime\":\"13:07\"}],\"Passed\":false}]},{\"id\":\"r-5097bb36-5bc4-4190-b4d5-c7edb925cb6f\",\"url\":\"https://reise.skyss.no/planner/travel-plan/r-5097bb36-5bc4-4190-b4d5-c7edb925cb6f\",\"StartTime\":\"2022-06-06T10:57:00.000Z\",\"EndTime\":\"2022-06-06T11:38:00.000Z\",\"End\":{\"Identifier\":\"NSR:Quay:54941\",\"Description\":\"Melkeplassen\",\"Location\":\"60.37585,5.30398\"},\"TravelSteps\":[{\"Type\":\"route\",\"StartTime\":\"2022-06-06T10:57:00.000Z\",\"EndTime\":\"2022-06-06T11:13:00.000Z\",\"Distance\":\"8281\",\"StopIdentifier\":\"NSR:Quay:55006\",\"Duration\":\"PT16M\",\"Status\":\"Late\",\"Path\":\"_lloJqp}^BCDEHEHBDJ@LATh@H@o@HeADEDIBMBO?Q?KAIH]HSNYRWVUVQXQVK`@Ml@Qp@S^KTMXORMNOPUN[L]L]H_@Jc@F_@Fg@Dy@Bm@?e@Ak@Ck@Ek@Gk@Ie@I_@Me@KYQa@Qa@AAIOEIYa@SYUYYWq@g@{@s@WQYUKKMIIIi@_@k@i@u@s@_@_@Y]Wa@m@}@c@w@SWUc@c@cAa@_A]s@]s@[e@]i@U[EEOQSWc@e@g@g@e@_@a@Yg@[y@c@s@_@c@UYKQGSOKIOMGGAMCKCIEGECECESEWC[AYAS@U@YBWDUFYHa@`@{AH]De@B_@@_@@c@A[CWE[Ic@GWGQIMGMSUKGMEKCM?UFe@NYFe@Hc@Dc@Fc@@c@?e@A{@Iu@Ea@Ca@?q@D[DaAPaBLWT_GTq@Ai@A_@Ci@Ki@Oo@[]Wc@_@s@s@kAoA{EmFaC}CsBuCkCgEgAmBw@wA_CmEiAuBc@y@_@w@]u@Ws@Qg@WgAUiASmAMoAIqAGuAIkDEoBIcBIeAMsAGqAUmAQw@GWEYEYIa@[{@EQOe@M[QQIGA??AGEKEGCWg@g@aAc@_Aa@mAUk@UwAM}@KaAIkAEaAImCAqC?mEBwE`C_gBlAc|@LcJFaH@wGCqFEoBIeCMiCMkBMaBUyBQ_B[uBe@iCc@oB_@}AOa@Sk@Um@Q]Yi@g@{@]o@sOmVMS]qAWiAGWM[OSQEQFC@CDGFGF?@MTG`@Gd@Cp@AdA@\\\\@j@LLH?TUHc@BYCe@I_@UkASwAK]KS{BeCsAy@c@[o@e@qE{EWYq@y@GGKMeBsBIKw@u@w@q@GEWWQSAAKK]_@Y[m@k@_@a@g@g@OOAAAAKKk@m@CGIGe@g@_Ay@OMKIAAGCMGKAOCCb@AZE\\\\UdBGd@YvBKx@KFC@M?ICWMa@WCASCOCaAg@]UA?GE{@i@MGUMQGMA[VIFMV]l@CDgAvBeAlCERCTGZKl@ELi@~AITm@dBg@xAGPCDA@OX[ZMXSh@g@nAEJIRSd@EJIRM\\\\s@hBK`AEfA?rAI~@G`@Of@]v@a@`AaAhB[\\\\wAx@i@j@Y\\\\}ArBSROBKGKSCYAa@Cw@QcBEc@Q_BOu@Mc@AAAIEIMc@]oAM_AEiADoADcA?i@Cc@Gc@[o@GOCEa@g@GGEE\",\"TripIdentifier\":\"SKY:ServiceJourney:3-158472-12710576\",\"CallIdentifier\":\"NSR:Quay:55006_SKY:ServiceJourney:3-158472-12710576_06062022-1257_stopNSR:Quay:53241_dt2022-06-06_\",\"RouteDirectionIdentifier\":\"SKY:Line:3~outbound\",\"RouteDirection\":{\"PublicIdentifier\":\"3\",\"DirectionName\":\"Til Støbotn\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:3~outbound\",\"PassingTimes\":[],\"Notes\":[]},\"Stop\":{\"Identifier\":\"NSR:Quay:55006\",\"Description\":\"Loddefjord terminal\",\"Location\":\"60.361744,5.235398\",\"ServiceModes\":[\"bus\"],\"RouteDirections\":[{\"PublicIdentifier\":\"3\",\"DirectionName\":\"Til Støbotn\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:3~outbound\",\"PassingTimes\":[],\"Notes\":[]}],\"Platform\":\"A\"},\"DisplayTime\":\"12:57\",\"Notes\":[],\"ExpectedEndTime\":\"2022-06-06T11:13:00.000Z\",\"Intermediates\":[{\"StopName\":\"Bjørndalsbakken\",\"Status\":\"Schedule\",\"AimedTime\":\"12:58\"},{\"StopName\":\"Tennebekk\",\"Status\":\"Schedule\",\"AimedTime\":\"12:59\"},{\"StopName\":\"Lyngbø rv. 555\",\"Status\":\"Schedule\",\"AimedTime\":\"13:03\"},{\"StopName\":\"Møhlenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"13:08\"},{\"StopName\":\"Festplassen\",\"Status\":\"Schedule\",\"AimedTime\":\"13:09\"},{\"StopName\":\"Torget\",\"Status\":\"Schedule\",\"AimedTime\":\"13:10\"},{\"StopName\":\"Bryggen\",\"Status\":\"Schedule\",\"AimedTime\":\"13:11\"},{\"StopName\":\"Bontelabo\",\"Status\":\"Schedule\",\"AimedTime\":\"13:13\"}],\"Passed\":false},{\"Type\":\"walk\",\"StartTime\":\"2022-06-06T11:13:00.000Z\",\"EndTime\":\"2022-06-06T11:13:23.000Z\",\"Distance\":\"29\",\"StopIdentifier\":\"NSR:Quay:53241\",\"Duration\":\"PT1M\",\"Status\":\"Schedule\",\"Path\":\"gjtoJkgn_@DO??DHHL??ITGR??EE??G`@\",\"Stop\":{\"Identifier\":\"NSR:Quay:53241\",\"Description\":\"Skutevikstorget\",\"Location\":\"60.402447,5.321026\",\"ServiceModes\":[\"bus\"]},\"Notes\":[],\"ExpectedEndTime\":\"2022-06-06T11:13:23.000Z\",\"Passed\":false},{\"Type\":\"route\",\"StartTime\":\"2022-06-06T11:19:00.000Z\",\"EndTime\":\"2022-06-06T11:38:00.000Z\",\"Distance\":\"6281\",\"StopIdentifier\":\"NSR:Quay:53240\",\"Duration\":\"PT19M\",\"Status\":\"Schedule\",\"Path\":\"ijtoJ_fn_@??DD`@f@BDFNZn@Fb@Bb@?h@EbAEnADhAL~@\\\\nALb@DHPn@Nt@P~ADb@@L?@NrABv@@`@BXJRJFNCRS|AsBX]h@k@vAy@Z]`AiB`@aA\\\\w@Ng@Fa@H_A?sADgAJaAr@iBL]HSDKRe@FQ@A@EBEf@oARi@LYZ[Ta@FQf@yAl@eBHUh@_BDMJm@F[BUDSdAmCfAwBBE@EDGT_@LWHGTAPBVNp@b@B@zA~@l@^VNJHf@\\\\B?\\\\RD@F?DABADIDKHa@d@qDLJDBz@n@j@d@PLHFh@b@h@d@FF|@~@TT`@`@\\\\b@XZ^^NPHJn@x@PTBB\\\\d@JLJL~AjBLLFFz@|@^b@dAjAfAt@T@TOXa@j@mB@In@d@b@ZrAx@zBdCJRJ\\\\RvATjAH^Bd@CXi@P?kA@c@BODMDKFEL@B@V^XZ??@BZRLTtP~WXl@LTRl@Pn@VbAJXJRBB?@@@DFJLJFJFRFL@R@RFNFHFLLh@f@LHhp@hn@dSdRlWvVbAbApAlA|@n@l@Xl@RTB^DPB|@FrAHzBJv@Hn@Pz@Z\\\\R\\\\Z@NBLDHFDGbAOjB_@O]SWWa@e@S]g@w@S_@SMOGgEa@sBS}@Ik@M[Ki@[k@i@e@m@e@s@c@k@OK][[_@k@}@k@u@_@][Oa@Km@I[M[QUOMK{BiBQ]o@mACEu@gAy@oAm@y@s@}@GIeBoBMMGGSQUQa@Wc@O??C?SEQA]BQDUJYRSRo@t@MJKFKD[FU@IAk@A??G?WBWAm@EGAK@YEYM}@U_Bg@}@a@UMi@WOIkAk@?A\",\"TripIdentifier\":\"SKY:ServiceJourney:19-158472-13331804\",\"CallIdentifier\":\"NSR:Quay:53240_SKY:ServiceJourney:19-158472-13331804_06062022-1319_stopNSR:Quay:54941_dt2022-06-06_\",\"WaitDuration\":\"PT6M\",\"RouteDirectionIdentifier\":\"SKY:Line:19~outbound\",\"RouteDirection\":{\"PublicIdentifier\":\"19\",\"DirectionName\":\"Til Melkeplassen\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:19~outbound\",\"PassingTimes\":[],\"Notes\":[]},\"Stop\":{\"Identifier\":\"NSR:Quay:53240\",\"Description\":\"Skutevikstorget\",\"Location\":\"60.402496,5.320633\",\"ServiceModes\":[\"bus\"],\"RouteDirections\":[{\"PublicIdentifier\":\"19\",\"DirectionName\":\"Til Melkeplassen\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:19~outbound\",\"PassingTimes\":[],\"Notes\":[]}]},\"Notes\":[],\"ExpectedEndTime\":\"2022-06-06T11:38:00.000Z\",\"Intermediates\":[{\"StopName\":\"Bontelabo\",\"Status\":\"Schedule\",\"AimedTime\":\"13:20\"},{\"StopName\":\"Bradbenken\",\"Status\":\"Schedule\",\"AimedTime\":\"13:22\"},{\"StopName\":\"Torget\",\"Status\":\"Schedule\",\"AimedTime\":\"13:24\"},{\"StopName\":\"Olav Kyrres gate\",\"Status\":\"Schedule\",\"AimedTime\":\"13:25\"},{\"StopName\":\"Møhlenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"13:28\"},{\"StopName\":\"Gyldenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"13:30\"},{\"StopName\":\"Løvstakktunnelen\",\"Status\":\"Schedule\",\"AimedTime\":\"13:34\"},{\"StopName\":\"Klauvsteinen\",\"Status\":\"Schedule\",\"AimedTime\":\"13:37\"},{\"StopName\":\"Hamrehaugen\",\"Status\":\"Schedule\",\"AimedTime\":\"13:37\"}],\"Passed\":false}]}]}"
        return travelPlannerResponseAdapter.fromJson(str)
    }

    fun getTravelPlanString1(): ApiTravelPlannerResponse? {
        val str = "{\"resultCode\":\"SUCCESS\",\"TravelPlans\":[{\"id\":\"r-677fdef5-54c6-49e7-b4dc-f85fcb4224ce\",\"url\":\"https://reise.skyss.no/planner/travel-plan/r-677fdef5-54c6-49e7-b4dc-f85fcb4224ce\",\"StartTime\":\"2022-06-24T21:36:00.000Z\",\"EndTime\":\"2022-06-24T22:37:00.000Z\",\"End\":{\"Description\":\"Petedalsflaten 34, Bergen\",\"Location\":\"60.30283,5.284152\"},\"TravelSteps\":[{\"Type\":\"route\",\"StartTime\":\"2022-06-24T21:36:00.000Z\",\"EndTime\":\"2022-06-24T21:49:00.000Z\",\"Distance\":\"6774\",\"StopIdentifier\":\"NSR:Quay:55006\",\"Duration\":\"PT13M\",\"Status\":\"Schedule\",\"Path\":\"_lloJqp}^BCDEHEHBDJ@LATh@H@o@HeADEDIBMBO?Q?KAIH]HSNYRWVUVQXQVK`@Ml@Qp@S^KTMXORMNOPUN[L]L]H_@Jc@F_@Fg@Dy@Bm@?e@Ak@Ck@Ek@Gk@Ie@I_@Me@KYQa@Qa@AAIOEIYa@SYUYYWq@g@{@s@WQYUKKMIIIi@_@k@i@u@s@_@_@Y]Wa@m@}@c@w@SWUc@c@cAa@_A]s@]s@[e@]i@U[EEOQSWc@e@g@g@e@_@a@Yg@[y@c@s@_@c@UYKQGSOKIOMGGAMCKCIEGECECESEWC[AYAS@U@YBWDUFYHa@`@{AH]De@B_@@_@@c@A[CWE[Ic@GWGQIMGMSUKGMEKCM?UFe@NYFe@Hc@Dc@Fc@@c@?e@A{@Iu@Ea@Ca@?q@D[DaAPaBLWT_GTq@Ai@A_@Ci@Ki@Oo@[]Wc@_@s@s@kAoA{EmFaC}CsBuCkCgEgAmBw@wA_CmEiAuBc@y@_@w@]u@Ws@Qg@WgAUiASmAMoAIqAGuAIkDEoBIcBIeAMsAGqAUmAQw@GWEYEYIa@[{@EQOe@M[QQIGA??AGEKEGCWg@g@aAc@_Aa@mAUk@UwAM}@KaAIkAEaAImCAqC?mEBwE`C_gBlAc|@LcJFaH@wGCqFEoBIeCMiCMkBMaBUyBQ_B[uBe@iCc@oB_@}AOa@Sk@Um@Q]Yi@g@{@]o@sOmVMS]qAWiAGWM[OSQEQFC@CDGFGF?@MTG`@Gd@Cp@AdA@\\\\@j@LLH?TUHc@BYCe@I_@UkASwAK]KS{BeCsAy@c@[o@e@qE{EWYq@y@GGKMeBsBIKw@u@w@q@GEWWQSAAKK]_@\",\"TripIdentifier\":\"SKY:ServiceJourney:3-160304-12707889\",\"CallIdentifier\":\"NSR:Quay:55006_SKY:ServiceJourney:3-160304-12707889_24062022-2336_stopNSR:Quay:53203_dt2022-06-24_\",\"RouteDirectionIdentifier\":\"SKY:Line:3~outbound\",\"RouteDirection\":{\"PublicIdentifier\":\"3\",\"DirectionName\":\"Til Støbotn\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:3~outbound\",\"PassingTimes\":[],\"Notes\":[]},\"Stop\":{\"Identifier\":\"NSR:Quay:55006\",\"Description\":\"Loddefjord terminal\",\"Location\":\"60.361744,5.235398\",\"ServiceModes\":[\"bus\"],\"RouteDirections\":[{\"PublicIdentifier\":\"3\",\"DirectionName\":\"Til Støbotn\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:3~outbound\",\"PassingTimes\":[],\"Notes\":[]}],\"Platform\":\"A\"},\"Notes\":[],\"ExpectedEndTime\":\"2022-06-24T21:49:00.000Z\",\"Occupancy\":\"LOW\",\"Intermediates\":[{\"StopName\":\"Bjørndalsbakken\",\"Status\":\"Schedule\",\"AimedTime\":\"23:37\"},{\"StopName\":\"Tennebekk\",\"Status\":\"Schedule\",\"AimedTime\":\"23:38\"},{\"StopName\":\"Lyngbø rv. 555\",\"Status\":\"Schedule\",\"AimedTime\":\"23:42\"},{\"StopName\":\"Møhlenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"23:48\"}],\"Passed\":false},{\"Type\":\"walk\",\"StartTime\":\"2022-06-24T21:49:00.000Z\",\"EndTime\":\"2022-06-24T21:50:52.000Z\",\"Distance\":\"144\",\"StopIdentifier\":\"NSR:Quay:53203\",\"Duration\":\"PT2M\",\"Status\":\"Schedule\",\"Path\":\"_croJqbo_@????]Q??CJ??AL??AD???@??@BBB@D@B@F@D?D@F?F?D??Mv@Mv@??EDG@G?EAEE??AF??CLAH??AD??MOEF??AI??YW??@C\",\"Stop\":{\"Identifier\":\"NSR:Quay:53203\",\"Description\":\"Festplassen\",\"Location\":\"60.391045,5.325373\",\"ServiceModes\":[\"bus\"],\"Platform\":\"M\"},\"Notes\":[],\"ExpectedEndTime\":\"2022-06-24T21:50:52.000Z\",\"Passed\":false},{\"Type\":\"route\",\"StartTime\":\"2022-06-24T22:09:00.000Z\",\"EndTime\":\"2022-06-24T22:34:00.000Z\",\"Distance\":\"11697\",\"StopIdentifier\":\"NSR:Quay:53118\",\"Duration\":\"PT25M\",\"Status\":\"Schedule\",\"Path\":\"ggroJo}n_@JL@@^^\\\\b@XZ^^NPHJn@x@PTBB\\\\d@JLJL~AjBLLFFz@|@^b@dAjAfAt@T@TOXa@j@mB@In@d@b@ZrAx@zBdCJRJ\\\\RvATjAH^Bd@CXi@P?kA@c@BODMDKFEL@B@V^XZ@B@@XPLTtP~WXl@LTRl@Pn@VbAJXJRBB?@@@DFJLJFJFRFL@R@RFNFHFLLh@f@LHhp@hn@dSdRlWvVbAbApAlA|@n@l@Xl@RfAL|@FrAHzBJv@Hn@Pz@Z\\\\R\\\\Z@NBLDHFDF@FCDIDK@Ob@RpBfAfBbAh@VPHTHb@JVHZNPHfAt@`BdAv@f@~@x@ZX^b@v@~@LNb@h@`@b@|@v@bAv@dAz@n@h@b@\\\\dAz@hAr@z@x@x@j@x@z@h@r@TRx@x@d@d@j@l@\\\\\\\\f@j@d@b@`@^VVZXJJJTLJRLRFNFJFLJ@HBHBDDDD?D?BCDEN?X@`@DXHb@L`@Rj@^PHRJBFBFDBD?DABEBIBI?K?MCKDgAFgAAm@ASEGAEQS{@u@ECQKKDCFADCT@VDLZXf@\\\\DBBBDDDFDHFP@NATCZCXEREDCHAHAJ@J@JBFBFDBD?HNLJLNf@^RZJTHVFVLr@PrAJl@H`BNxADnAFbBDvBDnB@r@DZDXJZCJAJ@J@JBHBFFBDADCBI\\\\f@b@d@x@r@xAbAdCxAjBtApAbAfAx@n@h@n@r@p@z@x@rAt@vA|@lB~@pBz@hBx@jBl@nAXd@CL?L?N@LDJDHDBD@DADCDGBINTZZ\\\\Rf@ZZPPD^L??RFfAFpBD~A?pA?xBGjBA~@Dx@Vv@^r@f@p@x@h@dAf@tAn@bB\\\\rAh@~BZ~A\\\\xAb@vAh@xAt@bBh@`BN~ABv@ET?T@RDPFLJFJ?JIJ?H@LDLJTVRX@?Z`@^d@LXJV?N@LBJBJBFDFDBD@FAFCL@FDFFHHLPdBnBb@f@JLl@`@JF|@_@rEoCrC}B~BmBtBqArDoBjGyB~C{@vA_@~B_@tDUlAQbBU|Ba@nFsBnGkEzCqBrBcAjAa@lAY`AMpAK~ACfBFpAPx@PbAZlAb@jB`AxAbAtAlArAtAjA|AlAjBjAlBzBdEz@jAD^v@jABFn@_DRo@R_@JCJMDQB[AYEUIOIGCO?SBQBOFIFEFAJ?J?LGFGd@{AZ_ABK?A@C@KBS?UAW@QDSFIFEF?FBFHDNJLL@LGHQDW?WAUGUEIIEDw@?y@Ai@Eu@Eu@My@O_Aa@wBa@kBUiAWu@Wi@[i@_@[c@]KGSQYWSUQWIM[c@??u@gAm@_Aq@sA`AeEZuANo@Rm@Rm@L_@P_@P_@RY\\\\c@HKLKVUVMj@WZM????x@WZIXGVEX?T?RDVDZJPJRNb@b@VXdBpB\\\\\\\\^VPJTHTBV@TCTERINK\\\\]\\\\o@Rg@Rs@Ls@NeAF_A@y@?]?G??As@?qBAoB?m@@c@@]Fi@Fm@Ns@Ni@Nc@P_@LUT[POROVMrAm@`Bw@|@[b@]Z]Xe@PUN_@Jk@TkAF_@TsA\",\"TripIdentifier\":\"SKY:ServiceJourney:50E-160588-12957753\",\"CallIdentifier\":\"NSR:Quay:53118_SKY:ServiceJourney:50E-160588-12957753_25062022-0009_stopNSR:Quay:51580_dt2022-06-24_\",\"WaitDuration\":\"PT19M\",\"RouteDirectionIdentifier\":\"SKY:Line:50E~outbound\",\"RouteDirection\":{\"PublicIdentifier\":\"50E\",\"DirectionName\":\"Til Birkelandsskiftet\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:50E~outbound\",\"PassingTimes\":[],\"Notes\":[]},\"Stop\":{\"Identifier\":\"NSR:Quay:53118\",\"Description\":\"Olav Kyrres gate\",\"Location\":\"60.391747,5.324475\",\"ServiceModes\":[\"bus\"],\"RouteDirections\":[{\"PublicIdentifier\":\"50E\",\"DirectionName\":\"Til Birkelandsskiftet\",\"ServiceMode\":\"Bus\",\"ServiceMode2\":\"Bus\",\"Identifier\":\"SKY:Line:50E~outbound\",\"PassingTimes\":[],\"Notes\":[]}],\"Platform\":\"G\"},\"Notes\":[],\"ExpectedEndTime\":\"2022-06-24T22:34:00.000Z\",\"Intermediates\":[{\"StopName\":\"Møhlenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"00:11\"},{\"StopName\":\"Gyldenpris\",\"Status\":\"Schedule\",\"AimedTime\":\"00:12\"},{\"StopName\":\"Oasen terminal\",\"Status\":\"Schedule\",\"AimedTime\":\"00:21\"},{\"StopName\":\"Fyllingsdalsveien\",\"Status\":\"Schedule\",\"AimedTime\":\"00:23\"},{\"StopName\":\"Sandeide terminal\",\"Status\":\"Schedule\",\"AimedTime\":\"00:25\"},{\"StopName\":\"Dolvik terminal\",\"Status\":\"Schedule\",\"AimedTime\":\"00:29\"},{\"StopName\":\"Dolvikhaugene\",\"Status\":\"Schedule\",\"AimedTime\":\"00:30\"},{\"StopName\":\"Eldsbakkane\",\"Status\":\"Schedule\",\"AimedTime\":\"00:31\"},{\"StopName\":\"Fanatorget\",\"Status\":\"Schedule\",\"AimedTime\":\"00:33\"}],\"Passed\":false},{\"Type\":\"walk\",\"StartTime\":\"2022-06-24T22:34:00.000Z\",\"EndTime\":\"2022-06-24T22:37:49.000Z\",\"Distance\":\"270\",\"StopIdentifier\":\"NSR:Quay:51580\",\"Duration\":\"PT4M\",\"Status\":\"Schedule\",\"Path\":\"st`oJqvg_@UrAEZKl@Ib@Qn@JRBHBL@PANCJENIPCDOd@AJW~@M^GJEDGDIB@B?`@AFCJK@YEI@Q?CDABCv@AN\",\"Stop\":{\"Identifier\":\"NSR:Quay:51580\",\"Description\":\"Petedalsheia\",\"Location\":\"60.301643,5.287546\",\"ServiceModes\":[\"bus\"]},\"Notes\":[],\"ExpectedEndTime\":\"2022-06-24T22:37:49.000Z\",\"Passed\":false}]}]}"
        return travelPlannerResponseAdapter.fromJson(str)
    }
}