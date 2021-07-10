package pl.janowicz.githubapidemo.features.userlist.ui

import pl.janowicz.githubapidemo.features.userlist.repository.User
import pl.janowicz.githubapidemo.utils.error.CallError

sealed class UsersScreenState {
    object Loading : UsersScreenState()
    data class Success(val users: List<User>) : UsersScreenState()
    data class Error(val callError: CallError) : UsersScreenState()
}
