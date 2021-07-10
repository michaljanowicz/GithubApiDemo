package pl.janowicz.githubapidemo.features.userlist.repository

import pl.janowicz.githubapidemo.api.ApiService
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val apiService: ApiService,
    private val apiUserToUserConverter: ApiUserToUserConverter
) {

    suspend fun getUsers(): List<User> =
        apiService.getUsers().map { apiUserToUserConverter.convert(it) }
}