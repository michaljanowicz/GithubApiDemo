package pl.janowicz.githubapidemo.features.userdetails.ui

import androidx.lifecycle.SavedStateHandle
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.janowicz.githubapidemo.TestCoroutineExtension
import pl.janowicz.githubapidemo.features.userdetails.repository.UserDetails
import pl.janowicz.githubapidemo.features.userdetails.repository.UserDetailsRepository
import pl.janowicz.githubapidemo.utils.error.CallError
import pl.janowicz.githubapidemo.utils.error.ThrowableToCallErrorConverter
import kotlin.time.ExperimentalTime

private const val KEY_USER_LOGIN = "user_login"
private const val EXAMPLE_LOGIN = "login"

@ExperimentalTime
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class, TestCoroutineExtension::class)
internal class UserDetailsViewModelTest {

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var userDetailsRepository: UserDetailsRepository

    @MockK
    private lateinit var throwableToCallErrorConverter: ThrowableToCallErrorConverter

    @InjectMockKs
    private lateinit var viewModel: UserDetailsViewModel

    @BeforeEach
    fun setUp() {
        every { savedStateHandle.get<String>(KEY_USER_LOGIN) } returns EXAMPLE_LOGIN
    }

    @Test
    fun `GIVEN ui state is not success and repository returns user details WHEN onCreated THEN loading and then success screen state`() {
        //given
        val userDetails = mockk<UserDetails>()
        coEvery { userDetailsRepository.getUserDetails(EXAMPLE_LOGIN) } returns userDetails

        viewModel.uiState.value shouldBeEqualTo UserDetailsScreenState.Loading

        //when
        viewModel.onCreated()

        //then
        viewModel.uiState.value shouldBeEqualTo UserDetailsScreenState.Success(userDetails)
    }

    @Test
    fun `GIVEN ui state is success WHEN onCreated THEN do nothing`() {
        //given
        val userDetailsOne = mockk<UserDetails>()
        coEvery { userDetailsRepository.getUserDetails(EXAMPLE_LOGIN) } returns userDetailsOne
        viewModel.onCreated()
        val userDetailsTwo = mockk<UserDetails>()
        coEvery { userDetailsRepository.getUserDetails(EXAMPLE_LOGIN) } returns userDetailsTwo

        //when
        viewModel.onCreated()

        //then
        viewModel.uiState.value shouldBeEqualTo UserDetailsScreenState.Success(userDetailsOne)
    }

    @Test
    fun `GIVEN ui state is not success and repository throws exception WHEN onCreated THEN show error ui event and error ui state`() =
        runBlockingTest {
            //given
            val exception = Exception()
            coEvery { userDetailsRepository.getUserDetails(EXAMPLE_LOGIN) } throws exception
            val callError = mockk<CallError>()
            every { throwableToCallErrorConverter.convert(exception) } returns callError

            //when
            viewModel.events.test {
                //when
                viewModel.onCreated()

                //then
                expectItem() shouldBeEqualTo UserDetailsScreenEvent.ShowError(callError)
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.uiState.value shouldBeEqualTo UserDetailsScreenState.Error
        }

    @Test
    fun `WHEN onBackClicked THEN loading and then success screen state`() = runBlockingTest {
        viewModel.events.test {
            //when
            viewModel.onBackClicked()

            //then
            expectItem() shouldBeEqualTo UserDetailsScreenEvent.GoBack
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN repository returns user details WHEN onSwipedRefresh THEN success ui state`() {
        //given
        val userDetails = mockk<UserDetails>()
        coEvery { userDetailsRepository.getUserDetails(EXAMPLE_LOGIN) } returns userDetails

        //when
        viewModel.onSwipedRefresh()

        //then
        viewModel.uiState.value shouldBeEqualTo UserDetailsScreenState.Success(userDetails)
    }
}