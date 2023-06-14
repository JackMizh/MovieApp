package app.submission.movieapp.data.repository

import app.submission.movieapp.data.dto.GenreListDto
import app.submission.movieapp.data.dto.MovieListDto
import app.submission.movieapp.data.dto.ReviewListDto
import app.submission.movieapp.data.dto.TrailerListDto
import app.submission.movieapp.data.models.Errors
import app.submission.movieapp.data.remote.MoviesService
import app.submission.movieapp.data.utils.Result
import app.submission.movieapp.domain.repository.MovieRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MoviesRepositoryImpl(
    private val service: MoviesService
): MovieRepository {
    override fun getGenres(): Flow<Result<GenreListDto>> = flow {
        emit(Result.Loading())

        val result = service.getDefaultGenres()

        if (result.isSuccessful) {
            result.body()?.let { emit(Result.Success(it)) }
        } else {
            val gson = Gson()
            val message: Errors = gson.fromJson(
                result.errorBody()!!.charStream(),
                Errors::class.java
            )
            emit(Result.Error(status_message = "Error ${message.status_code} : ${message.status_message}"))
        }
    }

    override fun getMovies(page: Int, with_genres: String): Flow<Result<MovieListDto>> = flow {
        emit(Result.Loading())

        val result = service.getDefaultMovies(page, with_genres)

        if (result.isSuccessful) {
            result.body()?.let { emit(Result.Success(it)) }
        } else {
            val gson = Gson()
            val message: Errors = gson.fromJson(
                result.errorBody()!!.charStream(),
                Errors::class.java
            )
            emit(Result.Error(status_message = "Error ${message.status_code} : ${message.status_message}"))
        }
    }

    override fun getReviews(id: Int, page: Int): Flow<Result<ReviewListDto>> = flow {
        emit(Result.Loading())

        val result = service.getDefaultReviews(id, page)

        if (result.isSuccessful) {
            result.body()?.let { emit(Result.Success(it)) }
        } else {
            val gson = Gson()
            val message: Errors = gson.fromJson(
                result.errorBody()!!.charStream(),
                Errors::class.java
            )
            emit(Result.Error(status_message = "Error ${message.status_code} : ${message.status_message}"))
        }
    }

    override fun getTrailers(id: Int, language: String): Flow<Result<TrailerListDto>> = flow {
        emit(Result.Loading())

        val result = service.getDefaultTrailers(id, language)

        if (result.isSuccessful) {
            result.body()?.let { emit(Result.Success(it)) }
        } else {
            val gson = Gson()
            val message: Errors = gson.fromJson(
                result.errorBody()!!.charStream(),
                Errors::class.java
            )
            emit(Result.Error(status_message = "Error ${message.status_code} : ${message.status_message}"))
        }
    }

}