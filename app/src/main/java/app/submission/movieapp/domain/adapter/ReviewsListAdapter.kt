package app.submission.movieapp.domain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.submission.movieapp.data.models.Reviews
import app.submission.movieapp.databinding.ItemMovieReviewBinding
import app.submission.movieapp.presentation.utils.getInitials


class ReviewsListAdapter: RecyclerView.Adapter<ReviewListViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Reviews>() {
        override fun areItemsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
            return oldItem.author == newItem.author
        }

        override fun areContentsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListViewHolder =
        ReviewListViewHolder(ItemMovieReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ReviewListViewHolder, position: Int) {
        val reviews = differ.currentList[holder.adapterPosition]
        holder.bind(reviews)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(reviews) }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Reviews) -> Unit)? = null
}

class ReviewListViewHolder(private val binding: ItemMovieReviewBinding): RecyclerView.ViewHolder(binding.root) {
    @Suppress("DEPRECATION")
    fun bind(reviews: Reviews) {
        with(binding) {
            author.text = reviews.author
            content.text = reviews.content

            initials.apply{
                text = getInitials(reviews.author!!)
            }
        }
    }
}