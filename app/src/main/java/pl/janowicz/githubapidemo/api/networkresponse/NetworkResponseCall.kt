package pl.janowicz.githubapidemo.api.networkresponse

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

class NetworkResponseCall<S : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, ApiError>
) : Call<NetworkResponse<S>> {

    private val throwableToNetworkResponseConverter = ThrowableToNetworkResponseConverter()

    override fun enqueue(callback: Callback<NetworkResponse<S>>) = delegate.enqueue(

        object : Callback<S> {

            override fun onResponse(call: Call<S>, response: Response<S>) {
                val networkResponse = if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResponse.Success(it)
                    } ?: NetworkResponse.ServerError()
                } else {
                    response.errorBody()?.let {
                        try {
                            val apiError = errorConverter.convert(it)
                            if (apiError == null) {
                                NetworkResponse.ServerError()
                            } else {
                                NetworkResponse.ApiError(apiError.message)
                            }
                        } catch (e: Exception) {
                            NetworkResponse.ServerError()
                        }
                    } ?: NetworkResponse.ServerError()
                }

                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(networkResponse)
                )
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(throwableToNetworkResponseConverter.convert(throwable))
                )
            }
        }
    )

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}