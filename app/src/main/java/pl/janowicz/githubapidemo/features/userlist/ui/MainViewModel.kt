package pl.janowicz.githubapidemo.features.userlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    init {
        getUsers()
    }

    fun onUserClicked(user: User) {
        //TODO
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
                UsersScreenState.Error(throwableToCallErrorConverter.convert(t))
            }
        }
    }
}