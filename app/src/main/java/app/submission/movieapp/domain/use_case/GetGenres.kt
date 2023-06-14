package app.submission.movieapp.domain.use_case

import app.submission.movieapp.data.models.Genres
import app.submission.movieapp.data.utils.Result
import app.submission.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetGenres(
    private val repository: MovieRepository
) {

    operator fun invoke(): Flow<Result<List<Genres>>> = repository.getGenres().map { result ->
        when (result) {
            is Result.Success -> {
                Result.Success(result.data?.toGenreList() ?: emptyList())
            }
            is Result.Error -> {
                Result.Error(status_message = result.status_message)
            }
            is Result.Loading -> Result.Loading()
        }
    }
}