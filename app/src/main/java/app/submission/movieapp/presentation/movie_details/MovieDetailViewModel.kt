package app.submission.movieapp.presentation.movie_details

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
class MovieDetailViewModel @Inject constructor(
    private val useCases: MovieUseCases
): ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state get(): StateFlow<MovieDetailState> = _state

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage get() = _errorMessage.asSharedFlow()

    fun loadMoreMovies() {
        if (state.value.allowLoadMore) {
            getDefaultReviews(state.value.movieId, state.value.currentPage + 1)
        }
    }

    fun changeMovie(id: Int){
        getDefaultTrailers(id)
    }

    private var jobTrailers: Job? = null
    private fun getDefaultTrailers(id: Int) {
        jobTrailers?.cancel()
        jobTrailers = useCases.getTrailers(id, "en-US")
            .onEach { result ->
                when (result) {
                    is app.submission.movieapp.data.utils.Result.Success -> {
                        _state.update {
                            var key = ""
                            for(i in result.data!!){
                                if(i.type == "Trailer"){
                                    key = i.key!!
                                }
                            }
                            it.copy(
                                isLoading = true,
                                review = it.review,
                                currentPage = it.currentPage,
                                movieId = id,
                                allowLoadMore = result.data.isNotEmpty(),
                                key = key
                            )
                        }
                        getDefaultReviews(id, state.value.currentPage)
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

    private var jobReviews: Job? = null
    private fun getDefaultReviews(id: Int, page: Int) {
        jobReviews?.cancel()
        jobReviews = useCases.getReview(id, page)
            .onEach { result ->
                when (result) {
                    is app.submission.movieapp.data.utils.Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                review = if (page > 1) {
                                    it.review + (result.data ?: emptyList())
                                } else {
                                    result.data ?: emptyList()
                                },
                                currentPage = page,
                                movieId = id,
                                allowLoadMore = (result.data ?: emptyList()).isNotEmpty(),
                                key = it.key
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