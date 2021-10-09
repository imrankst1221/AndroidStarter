package infixsoft.imrankst1221.android.starter.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author imran.choudhury
 * 18/9/21
 */

@Entity(
    tableName = "user_details",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["id"])
    ],
    indices = [Index("id")]
)
data class UserDetails(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val userId: Long,

    @SerializedName("login")
    val login: String,

    @ColumnInfo(defaultValue = "")
    @SerializedName("name")
    val name: String,

    @SerializedName("followers")
    val followers: Int,

    @SerializedName("following")
    val following: Int,

    @ColumnInfo(defaultValue = "")
    @SerializedName("company")
    var company: String,

    @ColumnInfo(defaultValue = "")
    @SerializedName("blog")
    val blog: String
) : Serializable