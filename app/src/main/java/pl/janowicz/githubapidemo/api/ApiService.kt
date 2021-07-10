package pl.janowicz.githubapidemo.api

import pl.janowicz.githubapidemo.api.model.ApiUser
import pl.janowicz.githubapidemo.api.model.ApiUserDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<ApiUser>

    @GET("users/{id}")
    suspend fun getUserDetails(@Path("id") id: Long): ApiUserDetails
}