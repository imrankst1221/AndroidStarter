package infixsoft.imrankst1221.android.starter.di

/**
 * @author imran.choudhury
 * 19/9/21
 *
 * DatabaseModule provider
 */

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import infixsoft.imrankst1221.android.starter.data.AppDatabase
import infixsoft.imrankst1221.android.starter.data.model.UserNoteDao
import infixsoft.imrankst1221.android.starter.data.model.UserDao
import infixsoft.imrankst1221.android.starter.data.model.UserDetailsDao
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
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDaoDao()
    }

    @Provides
    fun provideUserDetailsDao(appDatabase: AppDatabase): UserDetailsDao {
        return appDatabase.userDetailsDao()
    }

    @Provides
    fun provideUserNoteDao(appDatabase: AppDatabase): UserNoteDao {
        return appDatabase.userNoteDao()
    }
}
