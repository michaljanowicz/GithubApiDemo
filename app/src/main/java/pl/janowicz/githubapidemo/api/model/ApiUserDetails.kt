package pl.janowicz.githubapidemo.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiUserDetails(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String?,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "location") val location: String?,
    @Json(name = "blog") val blog: String?
)