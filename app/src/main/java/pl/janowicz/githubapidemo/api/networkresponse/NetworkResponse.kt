package pl.janowicz.githubapidemo.api.networkresponse

sealed class NetworkResponse<out T : Any> {

    data class Success<T : Any>(val body: T) : NetworkResponse<T>()

    data class ApiError(val message: String) : NetworkResponse<Nothing>()

    object NetworkError : NetworkResponse<Nothing>()

    data class ServerError(val error: Throwable? = null) : NetworkResponse<Nothing>()
}