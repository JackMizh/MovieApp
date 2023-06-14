package app.submission.movieapp.presentation.movie_details

import app.submission.movieapp.data.models.Reviews

data class MovieDetailState(
    val isLoading: Boolean = false,
    val review: List<Reviews> = emptyList(),
    val currentPage: Int = 1,
    val movieId: Int = 0,
    val allowLoadMore: Boolean = true,
    val key: String = ""
)