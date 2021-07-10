package pl.janowicz.githubapidemo.features.userlist.ui

import pl.janowicz.githubapidemo.features.userlist.repository.User

sealed class UsersScreenState {
    object Loading : UsersScreenState()
    object Error : UsersScreenState()
    data class Success(val users: List<User>) : UsersScreenState()
}
