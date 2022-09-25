package com.example.domain.utils

data class ResponseOutput<out T>(
    val status: Status,
    val data: T?,
    val error: ApiError?,
    val message: String?,
) {

    companion object {
        fun <T> success(data: T?): ResponseOutput<T> {
            return ResponseOutput(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: ApiError?): ResponseOutput<T> {
            return ResponseOutput(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): ResponseOutput<T> {
            return ResponseOutput(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}