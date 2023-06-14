package app.submission.movieapp.data.dto

import app.submission.movieapp.data.models.Trailers
import com.google.gson.annotations.SerializedName

data class TrailerListDto(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("results")
    val results: List<ResultItemsTrailer>,
) {
    fun toTrailerList(): List<Trailers> {
        return results.map { result ->
            Trailers(
                key = result.key,
                type = result.type
            )
        }
    }
}

data class ResultItemsTrailer(

    @field:SerializedName("key")
    val key: String? = null,

    @field:SerializedName("type")
    val type: String? = null,
)