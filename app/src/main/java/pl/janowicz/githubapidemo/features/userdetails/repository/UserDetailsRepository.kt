package pl.janowicz.githubapidemo.features.userdetails.repository

import pl.janowicz.githubapidemo.api.ApiService
import javax.inject.Inject

class UserDetailsRepository @Inject constructor(
    private val apiService: ApiService,
    private val apiUserDetailsToUserDetailsConverter: ApiUserDetailsToUserDetailsConverter
) {

    suspend fun getUserDetails(userLogin: String): UserDetails =
        apiUserDetailsToUserDetailsConverter.convert(apiService.getUserDetails(userLogin))
}