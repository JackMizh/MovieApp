package app.submission.movieapp.data.dto

import app.submission.movieapp.data.models.Movies
import com.google.gson.annotations.SerializedName

data class MovieListDto(

    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val movies: List<ResultItemsMovie>,
) {
    fun toMovieList(): List<Movies> {
        return movies.map { movies ->
            Movies(
                adult = movies.adult,
                backdrop_path = movies.backdrop_path,
                id = movies.id,
                original_language = movies.original_language,
                original_title = movies.original_title,
                overview = movies.overview,
                popularity = movies.popularity,
                poster_path = movies.poster_path,
                release_date = movies.release_date,
                title = movies.title,
                video = movies.video,
                vote_average = movies.vote_average,
                vote_count = movies.vote_count
            )
        }
    }
}

data class ResultItemsMovie(

    @field:SerializedName("adult")
    val adult: Boolean? = null,

    @field:SerializedName("backdrop_path")
    val backdrop_path: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("original_language")
    val original_language: String? = null,

    @field:SerializedName("original_title")
    val original_title: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("popularity")
    val popularity: Double? = null,

    @field:SerializedName("poster_path")
    val poster_path: String? = null,

    @field:SerializedName("release_date")
    val release_date: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("video")
    val video: Boolean? = null,

    @field:SerializedName("vote_average")
    val vote_average: Double? = null,

    @field:SerializedName("vote_count")
    val vote_count: Int? = null
)