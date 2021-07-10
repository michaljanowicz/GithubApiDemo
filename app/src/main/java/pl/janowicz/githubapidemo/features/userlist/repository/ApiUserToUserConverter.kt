package pl.janowicz.githubapidemo.features.userlist.repository

import pl.janowicz.githubapidemo.api.model.ApiUser
import javax.inject.Inject

class ApiUserToUserConverter @Inject constructor() {

    fun convert(apiUser: ApiUser) = User(
        id = apiUser.id,
        login = apiUser.login,
        avatarUrl = apiUser.avatarUrl
    )
}