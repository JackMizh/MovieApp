package app.submission.movieapp.domain.repository

import app.submission.movieapp.data.dto.GenreListDto
import app.submission.movieapp.data.dto.MovieListDto
import app.submission.movieapp.data.dto.ReviewListDto
import app.submission.movieapp.data.dto.TrailerListDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getGenres(): Flow<app.submission.movieapp.data.utils.Result<GenreListDto>>
    fun getMovies(page: Int, with_genres: String): Flow<app.submission.movieapp.data.utils.Result<MovieListDto>>
    fun getReviews(id: Int, page: Int): Flow<app.submission.movieapp.data.utils.Result<ReviewListDto>>
    fun getTrailers(id: Int, language: String): Flow<app.submission.movieapp.data.utils.Result<TrailerListDto>>
}