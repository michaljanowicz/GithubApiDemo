package pl.janowicz.githubapidemo.features.userdetails.ui

import pl.janowicz.githubapidemo.utils.error.CallError

sealed class UserDetailsScreenEvent {
    data class ShowError(val error: CallError) : UserDetailsScreenEvent()
    object GoBack : UserDetailsScreenEvent()
}
