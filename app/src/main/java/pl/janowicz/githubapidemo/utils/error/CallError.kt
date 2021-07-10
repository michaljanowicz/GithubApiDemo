package pl.janowicz.githubapidemo.utils.error

sealed class CallError {

    data class ApiError(val message: String) : CallError()

    object NetworkError : CallError()

    object UnknownError : CallError()
}