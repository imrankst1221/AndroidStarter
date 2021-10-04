package infixsoft.imrankst1221.android.starter.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author imran.choudhury
 * 4/10/21
 */

private const val KEY_SAVED_AT = "key_saved_at"

@Singleton
class PreferenceProvider @Inject internal constructor(
    context: Context) {
    private val appContext = context.applicationContext
    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun saveLastSavedAt(savedAt: String){
        preference.edit().putString(
            KEY_SAVED_AT,
            savedAt
        ).apply()
    }
    fun getLastSavedAt() : String?{
        return preference.getString(KEY_SAVED_AT,null)

    }
}