package pl.janowicz.githubapidemo.utils.error

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ThrowableToCallErrorConverter @Inject constructor(
    private val responseBodyToCallErrorConverter: ResponseBodyToCallErrorConverter
) {

    fun convert(throwable: Throwable) = when (throwable) {
        is ConnectException,
        is UnknownHostException,
        is SocketTimeoutException -> CallError.NetworkError
        is HttpException -> throwable.mapToNetworkResponse()
        else -> CallError.UnknownError
    }

    private fun HttpException.mapToNetworkResponse() = response()?.errorBody()?.let {
        responseBodyToCallErrorConverter.convert(it)
    } ?: CallError.UnknownError
}