package infixsoft.imrankst1221.android.starter.data.api

import infixsoft.imrankst1221.android.starter.BuildConfig
import infixsoft.imrankst1221.android.starter.data.model.User
import infixsoft.imrankst1221.android.starter.data.model.UserDetails
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import android.os.SystemClock
import okhttp3.Interceptor
import java.io.IOException


/**
 * @author imran.choudhury
 * 30/9/21
 */

interface UserApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int
    ): Response<ArrayList<User>>

    @GET("users/{user}")
    suspend fun getUserDetails(
        @Path("user") userName: String
    ): Response<UserDetails>


    companion object {
        fun create(): UserApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val dispatcher = Dispatcher().apply { maxRequests = 1 }
            val interceptor: Interceptor = object : Interceptor{
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    SystemClock.sleep(1000)
                    return chain.proceed(chain.request())
                }
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addNetworkInterceptor(interceptor)
                .dispatcher(dispatcher)
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