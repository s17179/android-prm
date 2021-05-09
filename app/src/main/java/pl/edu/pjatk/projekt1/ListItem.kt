package pl.edu.pjatk.projekt1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class ListItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "created_at") val createdAt: Date
) : Serializable