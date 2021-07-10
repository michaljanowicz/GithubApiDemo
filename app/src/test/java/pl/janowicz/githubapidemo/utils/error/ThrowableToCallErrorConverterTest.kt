package pl.janowicz.githubapidemo.utils.error

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import okhttp3.ResponseBody
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
internal class ThrowableToCallErrorConverterTest {

    @MockK
    private lateinit var responseBodyToCallErrorConverter: ResponseBodyToCallErrorConverter

    @InjectMockKs
    private lateinit var converter: ThrowableToCallErrorConverter

    @ParameterizedTest(name = "GIVEN {0} WHEN convert THEN {1}")
    @MethodSource("parametersProvider")
    fun convert(throwable: Throwable, expected: CallError) {
        //when
        val result = converter.convert(throwable)

        //then
        result shouldBeEqualTo expected
    }


    @Test
    fun `GIVEN http exception without error body WHEN convert THEN unknown error`() {
        //given
        val httpException = mockHttpException(errorBody = null)

        //when
        val result = converter.convert(httpException)

        //then
        result shouldBeEqualTo CallError.UnknownError
    }

    @Test
    fun `GIVEN http exception with error body and error converter returns call error WHEN convert THEN call error`() {
        //given
        val errorBody = mockk<ResponseBody>()
        val httpException = mockHttpException(errorBody = errorBody)
        val callError = mockk<CallError>()
        every { responseBodyToCallErrorConverter.convert(errorBody) } returns callError

        //when
        val result = converter.convert(httpException)

        //then
        result shouldBeEqualTo callError
    }

    private fun mockHttpException(errorBody: ResponseBody?) = mockk<HttpException> {
        every { response() } returns mockk {
            every { errorBody() } returns errorBody
        }
    }

    companion object {

        @JvmStatic
        fun parametersProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of(ConnectException(), CallError.NetworkError),
                Arguments.of(UnknownHostException(), CallError.NetworkError),
                Arguments.of(SocketTimeoutException(), CallError.NetworkError),
                Arguments.of(Exception(), CallError.UnknownError),
                Arguments.of(IllegalArgumentException(), CallError.UnknownError),
                Arguments.of(InterruptedException(), CallError.UnknownError)
            )
    }
}