package infixsoft.imrankst1221.android.starter.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import infixsoft.imrankst1221.android.starter.data.api.UserApiService
import javax.inject.Singleton

/**
 * @author imran.choudhury
 * 30/9/21
 */

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideUserApiService(): UserApiService {
        return UserApiService.create()
    }
}