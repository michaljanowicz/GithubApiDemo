package pl.janowicz.githubapidemo.api.networkresponse

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ThrowableToNetworkResponseConverter {

    fun convert(throwable: Throwable) = when (throwable) {
        is ConnectException,
        is UnknownHostException,
        is SocketTimeoutException -> NetworkResponse.NetworkError
        else -> NetworkResponse.ServerError(throwable)
    }
}