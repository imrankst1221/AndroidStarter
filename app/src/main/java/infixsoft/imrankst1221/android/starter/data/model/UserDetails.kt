package infixsoft.imrankst1221.android.starter.data.model

import androidx.room.*

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
    val userId: Long,
    val name: String,
    val followers: Int,
    val following: Int,
    var company: String,
    val blog: String,
    val note: String,
)