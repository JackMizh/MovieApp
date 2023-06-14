package app.submission.movieapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trailers(
    val key: String? = null,
    val type: String? = null,
): Parcelable