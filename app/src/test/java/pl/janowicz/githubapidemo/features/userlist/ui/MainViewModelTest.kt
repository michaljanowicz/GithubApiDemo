package pl.janowicz.githubapidemo.features.userlist.ui

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.janowicz.githubapidemo.TestCoroutineExtension
import pl.janowicz.githubapidemo.features.userlist.repository.User
import pl.janowicz.githubapidemo.features.userlist.repository.UsersRepository
import pl.janowicz.githubapidemo.utils.error.CallError
import pl.janowicz.githubapidemo.utils.error.ThrowableToCallErrorConverter
import kotlin.time.ExperimentalTime

private const val EXAMPLE_USER_LOGIN = "login"

@ExperimentalCoroutinesApi
@ExperimentalTime
@ExtendWith(MockKExtension::class, TestCoroutineExtension::class)
internal class MainViewModelTest {

    @MockK
    private lateinit var usersRepository: UsersRepository

    @MockK
    private lateinit var throwableToCallErrorConverter: ThrowableToCallErrorConverter

    @InjectMockKs
    private lateinit var viewModel: MainViewModel

    @Test
    fun `GIVEN ui state is not success and repository returns users WHEN onCreated THEN loading and then success screen state`() {
        //given
        val users = mockk<List<User>>()
        coEvery { usersRepository.getUsers() } returns users

        viewModel.uiState.value shouldBeEqualTo UsersScreenState.Loading

        //when
        viewModel.onCreated()

        //then
        viewModel.uiState.value shouldBeEqualTo UsersScreenState.Success(users)
    }

    @Test
    fun `GIVEN ui state is success WHEN onCreated THEN do nothing`() {
        //given
        val usersOne = mockk<List<User>>()
        coEvery { usersRepository.getUsers() } returns usersOne
        viewModel.onCreated()
        val usersTwo = mockk<List<User>>()
        coEvery { usersRepository.getUsers() } returns usersTwo

        //when
        viewModel.onCreated()

        //then
        viewModel.uiState.value shouldBeEqualTo UsersScreenState.Success(usersOne)
    }

    @Test
    fun `GIVEN ui state is not success and repository throws exception WHEN onCreated THEN show error ui event and error ui state`() =
        runBlockingTest {
            //given
            val exception = Exception()
            coEvery { usersRepository.getUsers() } throws exception
            val callError = mockk<CallError>()
            every { throwableToCallErrorConverter.convert(exception) } returns callError

            //when
            viewModel.events.test {
                //when
                viewModel.onCreated()

                //then
                expectItem() shouldBeEqualTo UsersScreenEvent.ShowError(callError)
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.uiState.value shouldBeEqualTo UsersScreenState.Error
        }

    @Test
    fun `WHEN onUserClicked THEN open user details screen event`() = runBlockingTest {
        //given
        val user = mockk<User> {
            every { login } returns EXAMPLE_USER_LOGIN
        }

        viewModel.events.test {
            //when
            viewModel.onUserClicked(user)

            //then
            expectItem() shouldBeEqualTo UsersScreenEvent.OpenUserDetails(userLogin = EXAMPLE_USER_LOGIN)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN repository returns users WHEN onSwipedRefresh THEN loading and then success screen state`() {
        //given
        val users = mockk<List<User>>()
        coEvery { usersRepository.getUsers() } returns users

        viewModel.uiState.value shouldBeEqualTo UsersScreenState.Loading

        //when
        viewModel.onSwipedRefresh()

        //then
        viewModel.uiState.value shouldBeEqualTo UsersScreenState.Success(users)
    }
}