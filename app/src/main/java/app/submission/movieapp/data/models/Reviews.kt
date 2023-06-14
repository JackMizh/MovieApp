package app.submission.movieapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reviews(
    val author: String? = null,
    val content: String? = null,
): Parcelable