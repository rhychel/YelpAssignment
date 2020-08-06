package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NetworkErrorException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException


suspend fun <T> safeApiCall(request: suspend () -> T): T {
    try {
        val result = request.invoke()
        if(result is Collection<*>)
            if(result.size == 0)
                throw NoDataException("No available data for $result")
        return request.invoke()
    } catch (e: HttpException) {
        val jsonObject = JSONObject(e.response()?.errorBody()?.string() ?: "{}")
        val errorJsonObject = jsonObject.getJSONObject("error")

        throw HttpRequestException(
            errorJsonObject.getString("code"),
            errorJsonObject.getString("description"),
            errorJsonObject.getString("field")
        )
    } catch (e: IOException) {
        throw NetworkErrorException(e)
    }
}
