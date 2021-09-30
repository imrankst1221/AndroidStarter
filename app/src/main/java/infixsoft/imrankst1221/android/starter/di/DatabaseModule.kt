package infixsoft.imrankst1221.android.starter.di

/**
 * @author imran.choudhury
 * 19/9/21
 */

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import infixsoft.imrankst1221.android.starter.data.AppDatabase
import infixsoft.imrankst1221.android.starter.data.model.UserDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePlantDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDaoDao()
    }
}
