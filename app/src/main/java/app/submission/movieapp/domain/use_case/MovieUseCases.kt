package app.submission.movieapp.domain.use_case

data class MovieUseCases(
    val getMovies: GetMovies,
    val getGenres: GetGenres,
    val getReview: GetReviews,
    val getTrailers: GetTrailers
)