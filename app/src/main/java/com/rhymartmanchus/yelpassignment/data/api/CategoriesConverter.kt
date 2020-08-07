package com.rhymartmanchus.yelpassignment.data.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.data.api.models.CategoryRaw
import java.lang.reflect.Type

class CategoriesConverter : JsonDeserializer<List<CategoryRaw>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<CategoryRaw> {
        val response = json.asJsonObject
        val categories = response["categories"]
        val gson = GsonBuilder().create()

        return categories.asJsonArray.map {
            val categoryJson = it.asJsonObject
            val raw = gson.fromJson(categoryJson, CategoryRaw::class.java)

            raw
        }
    }
}