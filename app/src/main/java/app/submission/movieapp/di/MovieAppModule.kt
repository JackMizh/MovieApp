package app.submission.movieapp.di

import app.submission.movieapp.data.remote.MoviesService
import app.submission.movieapp.data.repository.MoviesRepositoryImpl
import app.submission.movieapp.domain.repository.MovieRepository
import app.submission.movieapp.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MovieAppModule {

    @Provides
    @Singleton
    fun provideMovieService(): MoviesService {
        val client: OkHttpClient.Builder = OkHttpClient.Builder()
        client.connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)

        client.addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMWUyNzNmNzA4ZGNiNWRhM2M0ZGEyN2RkMGFlNjllOSIsInN1YiI6IjY0ODgyMTJjZTM3NWMwMDBhY2M2ZTNlOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.COKpoqNiG_7rVT0UIocavrotY8iRX0itMAxCOZDWP3Q"  // NOTE: This is unsafe, Normally I would put it in a separate file then access it from BuildConfig
                )
                .build()
            chain.proceed(request)
        }

        return Retrofit.Builder()
            .baseUrl(MoviesService.BASE_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(service: MoviesService): MovieRepository =
        MoviesRepositoryImpl(service = service)

    @Provides
    @Singleton
    fun provideUseCases(repository: MovieRepository): MovieUseCases =
        MovieUseCases(
            getMovies = GetMovies(repository),
            getGenres = GetGenres(repository),
            getReview = GetReviews(repository),
            getTrailers = GetTrailers(repository)
        )
}