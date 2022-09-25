package com.example.domain.datasource

import android.util.Log
import com.example.domain.utils.ApiError
import com.example.domain.utils.ResponseOutput
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

@Suppress("TooGenericExceptionCaught")
open class BaseRemoteDataSource constructor(
    private val retrofit: Retrofit,
) {
    suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String,
    ): ResponseOutput<T> {
        return try {
//            Log.d("zzz", "I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
//                Log.d("zzz", "isSuccessful response")
                return ResponseOutput.success(result.body())
            } else {
//                Log.d("zzz", "error response")
                val errorResponse = parseError(result)
                ResponseOutput.error(errorResponse?.statusMessage ?: defaultErrorMessage,
                    errorResponse)
            }
        } catch (e: Throwable) {
            ResponseOutput.error("Unknown Error $e", null)
        }
    }

    private fun parseError(response: Response<*>): ApiError? {
        val converter =
            retrofit.responseBodyConverter<ApiError>(ApiError::class.java, arrayOfNulls(0))
        return try {
            response.errorBody()?.let {
                converter.convert(it)
            }
        } catch (e: IOException) {
            ApiError()
        }
    }
}