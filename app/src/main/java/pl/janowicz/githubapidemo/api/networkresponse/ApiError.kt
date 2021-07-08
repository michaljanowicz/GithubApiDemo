package pl.janowicz.githubapidemo.api.networkresponse

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiError(@Json(name = "message") val message: String)