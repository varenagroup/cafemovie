package ir.teaching.cafemovie.server

import ir.teaching.cafemovie.data.Upcoming
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient.Builder


interface MovieApi {

    @GET("upcoming?api_key=b7cd3340a794e5a2f35e3abb820b497f")
    fun upcoming(
        @Query("page") page: Int
    ): Call<Upcoming>

    object RetrofitInstance {

        private val okHttpClient: OkHttpClient = Builder()
            .readTimeout(12, TimeUnit.SECONDS)
            .connectTimeout(12, TimeUnit.SECONDS)
            .build()

        val api: MovieApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MovieApi::class.java)
        }
    }
}