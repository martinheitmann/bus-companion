package com.app.skyss_companion.mappers

import com.app.skyss_companion.http.model.geocode.*
import com.app.skyss_companion.model.geocode.*

class GeocodeEntityMapper {
    companion object {
        fun mapApiGeocodingResponse(apiGeocodingResponse: ApiGeocodingResponse): GeocodingRoot {
            val geocoding: Geocoding = mapGeocodingResponse(apiGeocodingResponse.geocoding)
            val type: String = apiGeocodingResponse.type
            val features: List<GeocodingFeature> = apiGeocodingResponse.features.map { f -> mapGeocodingFeatureResponse(f) }
            val bbox: List<Float> = apiGeocodingResponse.bbox
            return GeocodingRoot(
                geocoding = geocoding,
                type = type,
                features = features,
                bbox = bbox
            )
        }

        private fun mapGeocodingResponse(geocodingResponse: GeocodingResponse): Geocoding {
            val version: String = geocodingResponse.version
            val attribution: String = geocodingResponse.attribution
            val query: Query = mapQueryResponse(geocodingResponse.query)
            val engine: Engine = mapEngineResponse(geocodingResponse.engine)
            val timestamp: Float = geocodingResponse.timestamp
            return Geocoding(
                version = version,
                attribution = attribution,
                query = query,
                engine = engine,
                timestamp = timestamp
            )
        }

        private fun mapEngineResponse(engineResponse: EngineResponse): Engine {
            val name: String = engineResponse.name
            val author: String = engineResponse.author
            val version: String = engineResponse.version
            return Engine(name = name, author = author, version = version)
        }

        private fun mapQueryResponse(queryResponse: QueryResponse): Query {
            val text: String = queryResponse.text
            val parser: String = queryResponse.parser
            val tokens: List<Any> = queryResponse.tokens
            val size: Float = queryResponse.size
            val layers: List<Any> = queryResponse.layers
            val sources: List<Any> = queryResponse.sources
            val private: Boolean = queryResponse.private
            val lang: Lang = mapLangResponse(queryResponse.lang)
            val querySize: Float = queryResponse.querySize
            return Query(
                text = text,
                parser = parser,
                tokens = tokens,
                size = size,
                layers = layers,
                sources = sources,
                private = private,
                lang = lang,
                querySize = querySize
            )
        }

        private fun mapLangResponse(langResponse: LangResponse): Lang {
            val name: String = langResponse.name
            val iso6391: String = langResponse.iso6391
            val iso6393: String = langResponse.iso6393
            val defaulted: Boolean = langResponse.defaulted
            return Lang(name = name, iso6391 = iso6391, iso6393 = iso6393, defaulted = defaulted)
        }

        private fun mapGeocodingFeatureResponse(geocodingFeatureResponse: GeocodingFeatureResponse): GeocodingFeature {
            val type: String = geocodingFeatureResponse.type
            val geometry: GeocodingGeometry = mapGeocodingGeometry(geocodingFeatureResponse.geometry)
            val properties: GeocodingProperties =  mapGeocodingProperties(geocodingFeatureResponse.properties)
            return GeocodingFeature(type = type, geometry = geometry, properties = properties)
        }

        private fun mapGeocodingGeometry(geocodingGeometryResponse: GeocodingGeometryResponse): GeocodingGeometry{
            val type: String = geocodingGeometryResponse.type
            val coordinates: List<Float> = geocodingGeometryResponse.coordinates
            return GeocodingGeometry(type = type, coordinates = coordinates)
        }

        private fun mapGeocodingProperties(geocodingPropertiesResponse: GeocodingPropertiesResponse): GeocodingProperties {
            val id: String? = geocodingPropertiesResponse.id
            val gid: String? = geocodingPropertiesResponse.gid
            val layer: String? = geocodingPropertiesResponse.layer
            val source: String? = geocodingPropertiesResponse.source
            val sourceId: String? = geocodingPropertiesResponse.source_id
            val name: String? = geocodingPropertiesResponse.name
            val street: String? = geocodingPropertiesResponse.street
            val accuracy: String? = geocodingPropertiesResponse.accuracy
            val countryA: String? = geocodingPropertiesResponse.country_a
            val county: String? = geocodingPropertiesResponse.county
            val countyGid: String? = geocodingPropertiesResponse.county_gid
            val locality: String? = geocodingPropertiesResponse.locality
            val localityGid: String? = geocodingPropertiesResponse.locality_gid
            val label: String? = geocodingPropertiesResponse.label
            val category: List<String> = geocodingPropertiesResponse.category
            return GeocodingProperties(
                id = id,
                gid = gid,
                layer = layer,
                source = source,
                sourceId = sourceId,
                name = name,
                street = street,
                accuracy = accuracy,
                countryA = countryA,
                county = county,
                countyGid = countyGid,
                locality = locality,
                localityGid = localityGid,
                label = label,
                category = category,
            )
        }
    }
}