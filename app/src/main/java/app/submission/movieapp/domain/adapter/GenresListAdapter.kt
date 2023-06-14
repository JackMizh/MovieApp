package app.submission.movieapp.domain.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.submission.movieapp.data.models.Genres
import app.submission.movieapp.databinding.ItemMovieGenreBinding


class GenreListAdapter: RecyclerView.Adapter<GenreListViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Genres>() {
        override fun areItemsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    var selectedItemIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreListViewHolder =
        GenreListViewHolder(ItemMovieGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: GenreListViewHolder, position: Int) {
        val news = differ.currentList[holder.adapterPosition]
        holder.bind(news)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(news) }
            news.isSelected = true
            if (selectedItemIndex != -1)
                differ.currentList[selectedItemIndex].isSelected = false
            selectedItemIndex = position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Genres) -> Unit)? = null
    fun setOnItemClickListener(listener: (Genres) -> Unit) {
        onItemClickListener = listener
    }
}

class GenreListViewHolder(private val binding: ItemMovieGenreBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(genres: Genres) {
        with(binding) {
            categoryTitle.text = genres.name
            if(genres.isSelected){
                categoryIndicator.visibility = View.VISIBLE
                categoryTitle.paint.shader = textShader(categoryTitle, true)
            }else{
                categoryIndicator.visibility = View.INVISIBLE
                categoryTitle.paint.shader = textShader(categoryTitle, false)
            }
        }
    }

    fun textShader(view: TextView, clicked: Boolean): Shader{
        val paint = view.paint
        val width = paint.measureText(view.text.toString())
        val textShader: Shader =
            if (clicked){
                LinearGradient(
                    0f,
                    0f,
                    width,
                    view.textSize,
                    intArrayOf(Color.parseColor("#FF8F71"), Color.parseColor("#EF2D1A")),
                    null,
                    Shader.TileMode.REPEAT
                )
            }
            else{
                LinearGradient(
                    0f,
                    0f,
                    width,
                    view.textSize,
                    intArrayOf(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF")),
                    null,
                    Shader.TileMode.REPEAT
                )
            }

        return textShader
    }
}