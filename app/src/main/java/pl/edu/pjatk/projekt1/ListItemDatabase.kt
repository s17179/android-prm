package pl.edu.pjatk.projekt1

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(ListItem::class), version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class ListItemDatabase : RoomDatabase() {
    abstract fun listItemDao(): ListItemDao
}