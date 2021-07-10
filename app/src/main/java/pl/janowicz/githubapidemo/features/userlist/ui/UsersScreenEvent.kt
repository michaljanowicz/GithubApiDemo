package pl.janowicz.githubapidemo.features.userlist.ui

import pl.janowicz.githubapidemo.utils.error.CallError

sealed class UsersScreenEvent {
    data class ShowError(val error: CallError) : UsersScreenEvent()
    data class OpenUserDetails(val userLogin: String) : UsersScreenEvent()
}
