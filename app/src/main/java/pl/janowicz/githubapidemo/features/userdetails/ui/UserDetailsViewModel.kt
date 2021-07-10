package pl.janowicz.githubapidemo.features.userdetails.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import pl.janowicz.githubapidemo.features.userdetails.repository.UserDetailsRepository
import pl.janowicz.githubapidemo.utils.error.ThrowableToCallErrorConverter
import pl.janowicz.githubapidemo.utils.extensions.requireValue
import javax.inject.Inject

const val KEY_USER_LOGIN = "user_login"

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userDetailsRepository: UserDetailsRepository,
    private val throwableToCallErrorConverter: ThrowableToCallErrorConverter
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserDetailsScreenState>(UserDetailsScreenState.Loading)
    val uiState: StateFlow<UserDetailsScreenState> = _uiState

    private val _events = MutableSharedFlow<UserDetailsScreenEvent>()
    val events = _events.asSharedFlow()

    private val userLogin: String get() = savedStateHandle.requireValue(KEY_USER_LOGIN)

    fun onCreated() {
        if (uiState.value !is UserDetailsScreenState.Success) {
            getUserDetails()
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            _uiState.value = try {
                UserDetailsScreenState.Success(userDetailsRepository.getUserDetails(userLogin))
            } catch (t: Throwable) {
                _events.emit(
                    UserDetailsScreenEvent.ShowError(
                        throwableToCallErrorConverter.convert(t)
                    )
                )
                UserDetailsScreenState.Error
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _events.emit(UserDetailsScreenEvent.GoBack)
        }
    }

    fun onSwipedRefresh() {
        _uiState.value = UserDetailsScreenState.Loading
        getUserDetails()
    }
}