package pl.janowicz.githubapidemo.features.userdetails.ui

import pl.janowicz.githubapidemo.features.userdetails.repository.UserDetails

sealed class UserDetailsScreenState {
    object Loading : UserDetailsScreenState()
    object Error : UserDetailsScreenState()
    data class Success(val userDetails: UserDetails) : UserDetailsScreenState()
}
