package app.submission.movieapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genres(
    val id: String? = null,
    val name: String? = null,
    var isSelected: Boolean = false
): Parcelable