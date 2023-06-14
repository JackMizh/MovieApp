package app.submission.movieapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Errors(
    val success: Boolean? = null,
    val status_code: Int? = null,
    var status_message: String? = null
): Parcelable