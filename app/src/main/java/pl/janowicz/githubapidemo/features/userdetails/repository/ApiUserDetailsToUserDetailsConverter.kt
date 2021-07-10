package pl.janowicz.githubapidemo.features.userdetails.repository

import pl.janowicz.githubapidemo.api.model.ApiUserDetails
import javax.inject.Inject

class ApiUserDetailsToUserDetailsConverter @Inject constructor() {

    fun convert(apiUserDetails: ApiUserDetails) = UserDetails(
        id = apiUserDetails.id,
        avatarUrl = apiUserDetails.avatarUrl,
        name = apiUserDetails.name ?: "",
        website = apiUserDetails.blog ?: "",
        location = apiUserDetails.location ?: ""
    )
}