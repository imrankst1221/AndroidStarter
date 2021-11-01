package infixsoft.imrankst1221.android.starter.data.model

/**
 * @author imran.choudhury
 * 8/10/21
 *
 * User note model
 */


import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "userNote",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["id"])
    ],
    indices = [Index("id")]
)

data class UserNote (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id", defaultValue = "")
    val noteId: Long,

    @ColumnInfo(defaultValue = "")
    val note: String
)