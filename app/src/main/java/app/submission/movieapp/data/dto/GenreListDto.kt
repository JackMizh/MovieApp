package app.submission.movieapp.data.dto

import app.submission.movieapp.data.models.Genres
import com.google.gson.annotations.SerializedName

data class GenreListDto (
    @field:SerializedName("genres")
    val genres: List<ResultItemsGenre>
){
    fun toGenreList(): List<Genres> {
        return genres.map { genre ->
            Genres(
                id = genre.id,
                name = genre.name
            )
        }
    }
}

data class ResultItemsGenre(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,
)