package app.submission.movieapp.domain.use_case

import app.submission.movieapp.data.models.Movies
import app.submission.movieapp.data.utils.Result
import app.submission.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMovies(
    private val repository: MovieRepository
) {

    operator fun invoke(page: Int, with_genres: String): Flow<Result<List<Movies>>> = repository.getMovies(page, with_genres).map { result ->
        when (result) {
            is Result.Success -> {
                Result.Success(result.data?.toMovieList() ?: emptyList())
            }
            is Result.Error -> {
                Result.Error(status_message = result.status_message)
            }
            is Result.Loading -> Result.Loading()
        }
    }
}