package app.submission.movieapp.domain.use_case

import app.submission.movieapp.data.models.Reviews
import app.submission.movieapp.data.utils.Result
import app.submission.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetReviews(
    private val repository: MovieRepository
) {

    operator fun invoke(id: Int, page: Int): Flow<Result<List<Reviews>>> = repository.getReviews(id, page).map { result ->
        when (result) {
            is Result.Success -> {
                Result.Success(result.data?.toReviewList() ?: emptyList())
            }
            is Result.Error -> {
                Result.Error(status_message = result.status_message)
            }
            is Result.Loading -> Result.Loading()
        }
    }
}