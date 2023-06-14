package app.submission.movieapp.data.utils

sealed class Result<T>(
    val data: T? = null,
    val status_message: String? = null
) {
    class Success<T>(data: T): Result<T>(data = data)
    class Error<T>(data: T? = null, status_message: String? = null): Result<T>(data, status_message)
    class Loading<T>(data: T? = null): Result<T>(data)
}