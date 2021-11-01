package infixsoft.imrankst1221.android.starter.data.model

/**
 * @author imran.choudhury
 * 18/9/21
 *
 * User model
 */

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var userId: Long = 0,

    @SerializedName("login")
    val login: String,

    @ColumnInfo(defaultValue = "")
    @SerializedName("avatar_url")
    val avatarUrl: String,

    @ColumnInfo(defaultValue = "")
    val userNote: String?,
) : Serializable