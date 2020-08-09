package com.rhymartmanchus.yelpassignment.data.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.data.api.models.OpenHourRaw
import java.lang.reflect.Type

class BusinessConverter : JsonDeserializer<BusinessRaw> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BusinessRaw {
        val response = json.asJsonObject
        val gson = GsonBuilder()
            .create()
        val raw = gson.fromJson(response, BusinessRaw::class.java)
        val openHours = if(response["hours"] == null) emptyList<OpenHourRaw>()
        else {
            gson.fromJson(response["hours"].asJsonArray[0].asJsonObject["open"].asJsonArray,
                object : TypeToken<MutableList<OpenHourRaw>>(){}.type)
        }

        return raw.copy(
            fullAddress = response["location"]
                .asJsonObject["display_address"]
                .asJsonArray
                .joinToString (separator = ", ") { dispAddress ->
                    dispAddress.asString
                },
            latitude = response["coordinates"].asJsonObject["latitude"].asDouble,
            longitude = response["coordinates"].asJsonObject["longitude"].asDouble,
            openHours = openHours
        )
    }
}