package com.rhymartmanchus.yelpassignment.data.api

import com.google.gson.*
import com.rhymartmanchus.yelpassignment.data.api.models.ReviewRaw
import java.lang.reflect.Type

class ReviewsConverter : JsonDeserializer<List<ReviewRaw>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<ReviewRaw> {
        val response = json.asJsonObject
        val reviews = response["reviews"]
        val gson = GsonBuilder().create()

        return reviews.asJsonArray.map {
            val reviewJson = it.asJsonObject
            val raw = gson.fromJson(reviewJson, ReviewRaw::class.java)
            val image_url = reviewJson["user"].asJsonObject.get("image_url")

            raw.copy(
                userName = reviewJson["user"].asJsonObject.get("name").asString,
                userImageUrl = if(image_url is JsonNull) null else image_url.asString
            )
        }
    }
}