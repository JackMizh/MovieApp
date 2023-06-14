package app.submission.movieapp.presentation.movie_list

import app.submission.movieapp.data.models.Genres
import app.submission.movieapp.data.models.Movies

data class MovieListState(
    val isLoading: Boolean = false,
    val movies: List<Movies> = emptyList(),
    val genres: List<Genres> = emptyList(),
    val currentPage: Int = 1,
    val allowLoadMore: Boolean = true,
    val selectedGenre: String = ""
)