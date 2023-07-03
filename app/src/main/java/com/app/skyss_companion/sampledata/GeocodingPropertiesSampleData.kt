package com.app.skyss_companion.sampledata

import com.app.skyss_companion.model.geocode.GeocodingProperties
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GeocodingPropertiesSampleData {

    companion object {
        fun getSample1(): List<GeocodingProperties> {
            val data = "[\n" +
                    "  {\n" +
                    "    \"accuracy\": \"point\",\n" +
                    "    \"category\": [\n" +
                    "      \"onstreetBus\"\n" +
                    "    ],\n" +
                    "    \"countryA\": \"NOR\",\n" +
                    "    \"county\": \"Vestland\",\n" +
                    "    \"countyGid\": \"whosonfirst:county:KVE:TopographicPlace:46\",\n" +
                    "    \"gid\": \"whosonfirst:venue:NSR:StopPlace:30904\",\n" +
                    "    \"id\": \"NSR:StopPlace:30904\",\n" +
                    "    \"label\": \"Festplassen, Bergen\",\n" +
                    "    \"layer\": \"venue\",\n" +
                    "    \"locality\": \"Bergen\",\n" +
                    "    \"localityGid\": \"whosonfirst:locality:KVE:TopographicPlace:4601\",\n" +
                    "    \"name\": \"Festplassen\",\n" +
                    "    \"source\": \"whosonfirst\",\n" +
                    "    \"sourceId\": \"NSR:StopPlace:30904\",\n" +
                    "    \"street\": \"NOT_AN_ADDRESS-NSR:StopPlace:30904\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"accuracy\": \"point\",\n" +
                    "    \"category\": [\n" +
                    "      \"onstreetBus\"\n" +
                    "    ],\n" +
                    "    \"countryA\": \"NOR\",\n" +
                    "    \"county\": \"Vestland\",\n" +
                    "    \"countyGid\": \"whosonfirst:county:KVE:TopographicPlace:46\",\n" +
                    "    \"gid\": \"whosonfirst:venue:NSR:StopPlace:30927\",\n" +
                    "    \"id\": \"NSR:StopPlace:30927\",\n" +
                    "    \"label\": \"Handelshøyskolen, Bergen\",\n" +
                    "    \"layer\": \"venue\",\n" +
                    "    \"locality\": \"Bergen\",\n" +
                    "    \"localityGid\": \"whosonfirst:locality:KVE:TopographicPlace:4601\",\n" +
                    "    \"name\": \"Handelshøyskolen\",\n" +
                    "    \"source\": \"whosonfirst\",\n" +
                    "    \"sourceId\": \"NSR:StopPlace:30927\",\n" +
                    "    \"street\": \"NOT_AN_ADDRESS-NSR:StopPlace:30927\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"accuracy\": \"point\",\n" +
                    "    \"category\": [\n" +
                    "      \"onstreetBus\"\n" +
                    "    ],\n" +
                    "    \"countryA\": \"NOR\",\n" +
                    "    \"county\": \"Vestland\",\n" +
                    "    \"countyGid\": \"whosonfirst:county:KVE:TopographicPlace:46\",\n" +
                    "    \"gid\": \"whosonfirst:venue:NSR:StopPlace:31935\",\n" +
                    "    \"id\": \"NSR:StopPlace:31935\",\n" +
                    "    \"label\": \"Vestre Vadmyra, Bergen\",\n" +
                    "    \"layer\": \"venue\",\n" +
                    "    \"locality\": \"Bergen\",\n" +
                    "    \"localityGid\": \"whosonfirst:locality:KVE:TopographicPlace:4601\",\n" +
                    "    \"name\": \"Vestre Vadmyra\",\n" +
                    "    \"source\": \"whosonfirst\",\n" +
                    "    \"sourceId\": \"NSR:StopPlace:31935\",\n" +
                    "    \"street\": \"NOT_AN_ADDRESS-NSR:StopPlace:31935\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"accuracy\": \"point\",\n" +
                    "    \"category\": [\n" +
                    "      \"street\"\n" +
                    "    ],\n" +
                    "    \"countryA\": \"NOR\",\n" +
                    "    \"county\": \"Vestland\",\n" +
                    "    \"countyGid\": \"whosonfirst:county:KVE:TopographicPlace:46\",\n" +
                    "    \"gid\": \"whosonfirst:address:KVE:TopographicPlace:4601-Vestre Vadmyra\",\n" +
                    "    \"id\": \"KVE:TopographicPlace:4601-Vestre Vadmyra\",\n" +
                    "    \"label\": \"Vestre Vadmyra, Bergen\",\n" +
                    "    \"layer\": \"address\",\n" +
                    "    \"locality\": \"Bergen\",\n" +
                    "    \"localityGid\": \"whosonfirst:locality:KVE:TopographicPlace:4601\",\n" +
                    "    \"name\": \"Vestre Vadmyra\",\n" +
                    "    \"source\": \"whosonfirst\",\n" +
                    "    \"sourceId\": \"KVE:TopographicPlace:4601-Vestre Vadmyra\",\n" +
                    "    \"street\": \"Vestre Vadmyra\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"accuracy\": \"point\",\n" +
                    "    \"category\": [\n" +
                    "      \"onstreetBus\"\n" +
                    "    ],\n" +
                    "    \"countryA\": \"NOR\",\n" +
                    "    \"county\": \"Vestland\",\n" +
                    "    \"countyGid\": \"whosonfirst:county:KVE:TopographicPlace:46\",\n" +
                    "    \"gid\": \"whosonfirst:venue:NSR:StopPlace:31806\",\n" +
                    "    \"id\": \"NSR:StopPlace:31806\",\n" +
                    "    \"label\": \"Gyldenpris, Bergen\",\n" +
                    "    \"layer\": \"venue\",\n" +
                    "    \"locality\": \"Bergen\",\n" +
                    "    \"localityGid\": \"whosonfirst:locality:KVE:TopographicPlace:4601\",\n" +
                    "    \"name\": \"Gyldenpris\",\n" +
                    "    \"source\": \"whosonfirst\",\n" +
                    "    \"sourceId\": \"NSR:StopPlace:31806\",\n" +
                    "    \"street\": \"NOT_AN_ADDRESS-NSR:StopPlace:31806\"\n" +
                    "  }\n" +
                    "]\n"
            val listType: Type = object : TypeToken<ArrayList<GeocodingProperties?>?>() {}.type
            return Gson().fromJson(data, listType)
        }
    }
}