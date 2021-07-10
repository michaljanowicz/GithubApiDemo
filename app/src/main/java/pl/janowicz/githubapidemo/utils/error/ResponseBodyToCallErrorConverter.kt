package pl.janowicz.githubapidemo.utils.error

import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import pl.janowicz.githubapidemo.api.model.ApiErrorJsonAdapter
import javax.inject.Inject

class ResponseBodyToCallErrorConverter @Inject constructor(moshi: Moshi) {

    private val errorAdapter = ApiErrorJsonAdapter(moshi)

    fun convert(responseBody: ResponseBody) =
        errorAdapter.fromJson(responseBody.string())?.let {
            CallError.ApiError(it.message)
        } ?: CallError.UnknownError
}