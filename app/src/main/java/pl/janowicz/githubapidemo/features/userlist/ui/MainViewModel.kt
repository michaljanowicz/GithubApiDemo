package pl.janowicz.githubapidemo.features.userlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import pl.janowicz.githubapidemo.features.userlist.repository.User
import pl.janowicz.githubapidemo.features.userlist.repository.UsersRepository
import pl.janowicz.githubapidemo.utils.error.ThrowableToCallErrorConverter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val throwableToCallErrorConverter: ThrowableToCallErrorConverter
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersScreenState>(UsersScreenState.Loading)
    val uiState: StateFlow<UsersScreenState> = _uiState

    private val _events = MutableSharedFlow<UsersScreenEvent>()
    val events = _events.asSharedFlow()

    fun onCreated() {
        if (uiState.value !is UsersScreenState.Success) {
            getUsers()
        }
    }

    fun onUserClicked(user: User) {
        viewModelScope.launch {
            _events.emit(UsersScreenEvent.OpenUserDetails(userLogin = user.login))
        }
    }

    fun onSwipedRefresh() {
        _uiState.value = UsersScreenState.Loading
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            _uiState.value = try {
                UsersScreenState.Success(usersRepository.getUsers())
            } catch (t: Throwable) {
                _events.emit(UsersScreenEvent.ShowError(throwableToCallErrorConverter.convert(t)))
                UsersScreenState.Error
            }
        }
    }
}