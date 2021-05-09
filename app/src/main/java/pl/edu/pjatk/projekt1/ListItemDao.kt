package pl.edu.pjatk.projekt1

import android.database.Cursor
import androidx.room.*

@Dao
interface ListItemDao {
    @Query("SELECT * FROM ListItem ORDER BY created_at ASC")
    fun getAll(): List<ListItem>

    @Insert
    fun insert(listItem: ListItem)

    @Update
    fun update(listItem: ListItem)

    @Delete
    fun delete(listItem: ListItem)

    @Query("SELECT * FROM ListItem WHERE id = :id")
    fun getById(id: Long): Cursor
}