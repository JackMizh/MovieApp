package app.submission.movieapp.data.dto

import app.submission.movieapp.data.models.Reviews
import com.google.gson.annotations.SerializedName

data class ReviewListDto(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val results: List<ResultItemsReview>,
) {
    fun toReviewList(): List<Reviews> {
        return results.map { result ->
            Reviews(
                author = result.author,
                content = result.content
            )
        }
    }
}

data class ResultItemsReview(

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("content")
    val content: String? = null,
)