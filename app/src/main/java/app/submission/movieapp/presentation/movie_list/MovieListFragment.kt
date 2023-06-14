package app.submission.movieapp.presentation.movie_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.submission.movieapp.databinding.FragmentMovieListBinding
import app.submission.movieapp.domain.adapter.GenreListAdapter
import app.submission.movieapp.domain.adapter.MoviesListAdapter
import app.submission.movieapp.presentation.utils.MarginItemDecorationVertical
import app.submission.movieapp.presentation.utils.listenForPagination
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val viewModelMovieList by viewModels<MovieListViewModel>()
    private var _binding: FragmentMovieListBinding? = null
    private lateinit var moviesAdapter: MoviesListAdapter
    private lateinit var genresAdapter: GenreListAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        moviesAdapter = MoviesListAdapter()
        genresAdapter = GenreListAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rv.apply {
                adapter = moviesAdapter
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                addItemDecoration(MarginItemDecorationVertical(60, 30))
                listenForPagination {
                    viewModelMovieList.loadMoreMovies()
                }
            }

            rvCategory.apply {
                adapter = genresAdapter
            }

            moviesAdapter.setOnItemClickListener {
                val dir = MovieListFragmentDirections.actionListMoviesToDetailMovie(it)
                findNavController().navigate(dir)
            }

            genresAdapter.setOnItemClickListener {
                viewModelMovieList.changeGenre(it.id.toString())
            }

            @Suppress("DEPRECATION")
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModelMovieList.state.collectLatest { state ->
                            moviesAdapter.differ.submitList(state.movies)
                            genresAdapter.differ.submitList(state.genres)
                            if (state.isLoading) {
                                progressBar.visibility = View.VISIBLE
                            } else {
                                progressBar.visibility = View.GONE
                            }
                        }
                    }

                    launch {
                        viewModelMovieList.errorMessage.collectLatest { message ->
                            Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}