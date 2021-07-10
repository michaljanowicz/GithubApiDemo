package pl.janowicz.githubapidemo.features.userdetails.repository

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
import pl.janowicz.githubapidemo.api.ApiService
import pl.janowicz.githubapidemo.api.model.ApiUserDetails

private const val EXAMPLE_LOGIN = "login"

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class UserDetailsRepositoryTest {

    @MockK
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var apiUserDetailsToUserDetailsConverter: ApiUserDetailsToUserDetailsConverter

    @InjectMockKs
    private lateinit var repository: UserDetailsRepository

    @Test
    fun `GIVEN api returns api user details and converter returns user details WHEN getUserDetails THEN user details`() =
        runBlockingTest {
            //given
            val apiUserDetails = mockk<ApiUserDetails>()
            val userDetails = mockk<UserDetails>()
            coEvery { apiService.getUserDetails(EXAMPLE_LOGIN) } returns apiUserDetails
            every { apiUserDetailsToUserDetailsConverter.convert(apiUserDetails) } returns userDetails

            //when
            val result = repository.getUserDetails(EXAMPLE_LOGIN)

            //then
            result shouldBeEqualTo userDetails
        }
}