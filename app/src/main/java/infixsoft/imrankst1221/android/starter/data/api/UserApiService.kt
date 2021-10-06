package infixsoft.imrankst1221.android.starter.data.api

import infixsoft.imrankst1221.android.starter.BuildConfig
import infixsoft.imrankst1221.android.starter.data.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author imran.choudhury
 * 30/9/21
 */

interface UserApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int
    ): Response<List<User>>

    companion object {
        fun create(): UserApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApiService::class.java)
        }
    }

}