package pl.janowicz.githubapidemo.features.userdetails.repository

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import pl.janowicz.githubapidemo.api.model.ApiUserDetails

private const val EXAMPLE_ID = 123L
private const val EXAMPLE_AVATAR_URL = "avatar"
private const val EXAMPLE_NAME = "name"
private const val EXAMPLE_WEBSITE = "website"
private const val EXAMPLE_LOCATION = "location"

internal class ApiUserDetailsToUserDetailsConverterTest {

    private lateinit var converter: ApiUserDetailsToUserDetailsConverter

    @BeforeEach
    fun setUp() {
        converter = ApiUserDetailsToUserDetailsConverter()
    }

    @Test
    fun `GIVEN api user details WHEN convert THEN user details`() {
        //given
        val apiUserDetails = ApiUserDetails(
            id = EXAMPLE_ID,
            avatarUrl = EXAMPLE_AVATAR_URL,
            name = EXAMPLE_NAME,
            blog = EXAMPLE_WEBSITE,
            location = EXAMPLE_LOCATION
        )

        //when
        val result = converter.convert(apiUserDetails)

        //then
        result shouldBeEqualTo UserDetails(
            id = EXAMPLE_ID,
            avatarUrl = EXAMPLE_AVATAR_URL,
            name = EXAMPLE_NAME,
            website = EXAMPLE_WEBSITE,
            location = EXAMPLE_LOCATION
        )
    }

    @Test
    fun `GIVEN api user details with null fields WHEN convert THEN user details with empty string fields`() {
        //given
        val apiUserDetails = ApiUserDetails(
            id = EXAMPLE_ID,
            avatarUrl = EXAMPLE_AVATAR_URL,
            name = null,
            blog = null,
            location = null
        )

        //when
        val result = converter.convert(apiUserDetails)

        //then
        result shouldBeEqualTo UserDetails(
            id = EXAMPLE_ID,
            avatarUrl = EXAMPLE_AVATAR_URL,
            name = "",
            website = "",
            location = ""
        )
    }
}