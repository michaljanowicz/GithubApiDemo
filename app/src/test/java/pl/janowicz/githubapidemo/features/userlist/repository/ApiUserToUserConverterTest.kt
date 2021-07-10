package pl.janowicz.githubapidemo.features.userlist.repository

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.janowicz.githubapidemo.api.model.ApiUser

private const val EXAMPLE_ID = 123L
private const val EXAMPLE_LOGIN = "login"
private const val EXAMPLE_AVATAR_URL = "avatar"

internal class ApiUserToUserConverterTest {

    private lateinit var converter: ApiUserToUserConverter

    @BeforeEach
    fun setUp() {
        converter = ApiUserToUserConverter()
    }

    @Test
    fun `GIVEN api user WHEN convert THEN user`() {
        //given
        val apiUser = ApiUser(
            id = EXAMPLE_ID,
            login = EXAMPLE_LOGIN,
            avatarUrl = EXAMPLE_AVATAR_URL
        )

        //when
        val result = converter.convert(apiUser)

        //then
        result shouldBeEqualTo User(
            id = EXAMPLE_ID,
            login = EXAMPLE_LOGIN,
            avatarUrl = EXAMPLE_AVATAR_URL
        )
    }
}