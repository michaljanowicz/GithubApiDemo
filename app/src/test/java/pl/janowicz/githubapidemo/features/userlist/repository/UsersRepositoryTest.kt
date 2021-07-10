package pl.janowicz.githubapidemo.features.userlist.repository

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
import pl.janowicz.githubapidemo.api.model.ApiUser

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class UsersRepositoryTest {

    @MockK
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var apiUserToUserConverter: ApiUserToUserConverter

    @InjectMockKs
    private lateinit var repository: UsersRepository

    @Test
    fun `GIVEN api returns api users and converter returns user WHEN getUsers THEN users`() =
        runBlockingTest {
            //given
            val apiUserOne = mockk<ApiUser>()
            val apiUserTwo = mockk<ApiUser>()
            val userOne = mockk<User>()
            val userTwo = mockk<User>()
            coEvery { apiService.getUsers() } returns listOf(apiUserOne, apiUserTwo)

            every { apiUserToUserConverter.convert(apiUserOne) } returns userOne
            every { apiUserToUserConverter.convert(apiUserTwo) } returns userTwo

            //when
            val result = repository.getUsers()

            result shouldBeEqualTo listOf(userOne, userTwo)
        }
}