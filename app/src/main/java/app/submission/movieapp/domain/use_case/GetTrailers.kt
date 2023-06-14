package app.submission.movieapp.domain.use_case

import app.submission.movieapp.data.models.Trailers
import app.submission.movieapp.data.utils.Result
import app.submission.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTrailers(
    private val repository: MovieRepository
) {

    operator fun invoke(id: Int, language: String): Flow<Result<List<Trailers>>> = repository.getTrailers(id, language).map { result ->
        when (result) {
            is Result.Success -> {
                Result.Success(result.data?.toTrailerList() ?: emptyList())
            }
            is Result.Error -> {
                Result.Error(status_message = result.status_message)
            }
            is Result.Loading -> Result.Loading()
        }
    }
}