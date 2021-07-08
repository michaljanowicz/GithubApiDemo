package pl.janowicz.githubapidemo.api.networkresponse

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class NetworkResponseAdapter<S : Any>(
    private val successType: Type,
    private val errorConverter: Converter<ResponseBody, ApiError>
) : CallAdapter<S, Call<NetworkResponse<S>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResponse<S>> =
        NetworkResponseCall(call, errorConverter)

}