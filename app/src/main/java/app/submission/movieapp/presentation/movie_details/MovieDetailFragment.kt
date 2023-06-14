package app.submission.movieapp.presentation.movie_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.submission.movieapp.databinding.FragmentMovieDetailBinding
import app.submission.movieapp.domain.adapter.ReviewsListAdapter
import app.submission.movieapp.presentation.utils.MarginItemDecorationVertical
import app.submission.movieapp.presentation.utils.listenForPaginationLinear
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private val viewModel by viewModels<MovieDetailViewModel>()
    private var _binding: FragmentMovieDetailBinding? = null
    private lateinit var reviewAdapter: ReviewsListAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        reviewAdapter = ReviewsListAdapter()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val movies = MovieDetailFragmentArgs.fromBundle(it).movies

            with(binding) {
                title.text = movies.title
                popularity.text = (movies.popularity).toString()
                rating.text = "${movies.vote_average} (${movies.vote_count} Votes)"
                synopsis.text = movies.overview

//                viewModel.changeMovie((movies.id)!!.toInt())
                viewModel.changeMovie(0)

                rvReview.apply{
                    adapter = reviewAdapter
                    isNestedScrollingEnabled = false
                    addItemDecoration(MarginItemDecorationVertical(60, 0))
                    listenForPaginationLinear {
                        viewModel.loadMoreMovies()
                    }
                }

                fun setVideoPayer(id: String){
                    lifecycle.addObserver(youtubePlayerView)
                    youtubePlayerView.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(id, 0f)
                        }
                    })
                }

                @Suppress("DEPRECATION")
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        launch {
                            viewModel.state.collectLatest { state ->
                                reviewAdapter.differ.submitList(state.review)
                                Log.d("Type", state.key)
                                if(state.key != ""){
                                    setVideoPayer(state.key)
                                }
                            }
                        }

                        launch {
                            viewModel.errorMessage.collectLatest { message ->
                                Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
                            }
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