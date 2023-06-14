package app.submission.movieapp.data.remote

import app.submission.movieapp.data.dto.GenreListDto
import app.submission.movieapp.data.dto.MovieListDto
import app.submission.movieapp.data.dto.ReviewListDto
import app.submission.movieapp.data.dto.TrailerListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("3/discover/movie")
    suspend fun getDefaultMovies(
        @Query("page") page: Int,
        @Query("with_genres") with_genres: String
    ): Response<MovieListDto>

    @GET("3/genre/movie/list")
    suspend fun getDefaultGenres(
    ): Response<GenreListDto>

    @GET("3/movie/{id}/reviews")
    suspend fun getDefaultReviews(
        @Path("id") id: Int,
        @Query("page") page: Int,
    ): Response<ReviewListDto>

    @GET("3/movie/{id}/videos")
    suspend fun getDefaultTrailers(
        @Path("id") id: Int,
        @Query("language") language: String,
    ): Response<TrailerListDto>

    // NOTE: this is unsafe, normally I put it in a separate file and then access it later from BuildConfig
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/"
    }
}