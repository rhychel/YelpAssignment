package com.rhymartmanchus.yelpassignment.data.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import java.lang.reflect.Type

class BusinessesConverter : JsonDeserializer<List<BusinessRaw>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<BusinessRaw> {
        val response = json.asJsonObject
        val businesses = response["businesses"]
        val gson = GsonBuilder().create()

        return businesses.asJsonArray.map {
            val businessJson = it.asJsonObject
            val raw = gson.fromJson(businessJson, BusinessRaw::class.java)

            raw.copy(
                fullAddress = businessJson["location"]
                    .asJsonObject["display_address"]
                    .asJsonArray
                    .joinToString (separator = ", ") { dispAddress ->
                        dispAddress.asString
                    },
                latitude = businessJson["coordinates"].asJsonObject["latitude"].asDouble,
                longitude = businessJson["coordinates"].asJsonObject["longitude"].asDouble
            )
        }
    }
}