package app.submission.movieapp.domain.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.submission.movieapp.data.models.Movies
import app.submission.movieapp.databinding.ItemMovieCardBinding
import app.submission.movieapp.presentation.utils.loadImage

class MoviesListAdapter: RecyclerView.Adapter<MovieListViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder =
        MovieListViewHolder(ItemMovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val news = differ.currentList[holder.adapterPosition]
        holder.bind(news)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(news) }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Movies) -> Unit)? = null
    fun setOnItemClickListener(listener: (Movies) -> Unit) {
        onItemClickListener = listener
    }
}

class MovieListViewHolder(private val binding: ItemMovieCardBinding): RecyclerView.ViewHolder(binding.root) {
    @Suppress("DEPRECATION")
    fun bind(movies: Movies) {
        with(binding) {
            val title = "<font color=#ffffff>" + movies.title + "</font> <font color=#aaaaaa>(" + (movies.release_date)!!.substring(0,4) + ")</font>"
            imvBanner.loadImage("https://image.tmdb.org/t/p/original/" + movies.poster_path)
            tvTitle.text = Html.fromHtml(title)
        }
    }
}