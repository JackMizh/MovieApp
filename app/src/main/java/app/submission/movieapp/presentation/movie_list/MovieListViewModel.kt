package app.submission.movieapp.presentation.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.submission.movieapp.domain.use_case.MovieUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val useCases: MovieUseCases
): ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state get(): StateFlow<MovieListState> = _state

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage get() = _errorMessage.asSharedFlow()

    init {
        getDefaultGenres(state.value.selectedGenre)
    }

    private var jobGenres: Job? = null
    private fun getDefaultGenres(with_genres: String) {
        jobGenres?.cancel()
        jobGenres = useCases.getGenres()
            .onEach { result ->
                when (result) {
                    is app.submission.movieapp.data.utils.Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                movies = it.movies,
                                genres = it.genres + (result.data ?: emptyList()),
                                currentPage = it.currentPage,
                                allowLoadMore = (result.data ?: emptyList()).isNotEmpty(),
                                selectedGenre = with_genres
                            )
                        }
                        getDefaultMovies(1, with_genres)
                    }
                    is app.submission.movieapp.data.utils.Result.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        result.status_message?.let { _errorMessage.emit(it) }
                    }
                    is app.submission.movieapp.data.utils.Result.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
            .launchIn(viewModelScope.plus(Dispatchers.IO))
    }

    fun loadMoreMovies() {
        if (state.value.allowLoadMore) {
            getDefaultMovies(state.value.currentPage + 1, state.value.selectedGenre)
        }
    }

    fun changeGenre(with_genres: String){
        _state.update {
            it.copy(
                isLoading = true,
                movies = emptyList(),
                genres = it.genres,
                currentPage = 1,
                allowLoadMore = true,
                selectedGenre = with_genres
            )
        }
        getDefaultMovies(1, with_genres)
    }

    private var jobMovies: Job? = null
    private fun getDefaultMovies(page: Int, with_genres: String) {
        jobMovies?.cancel()
        jobMovies = useCases.getMovies(page, with_genres)
            .onEach { result ->
                when (result) {
                    is app.submission.movieapp.data.utils.Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                movies = if (page > 1) {
                                    it.movies + (result.data ?: emptyList())
                                } else {
                                    result.data ?: emptyList()
                                },
                                currentPage = page,
                                allowLoadMore = (result.data ?: emptyList()).isNotEmpty(),
                                selectedGenre = with_genres
                            )
                        }
                    }
                    is app.submission.movieapp.data.utils.Result.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        result.status_message?.let { _errorMessage.emit(it) }
                    }
                    is app.submission.movieapp.data.utils.Result.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
            .launchIn(viewModelScope.plus(Dispatchers.IO))
    }
}