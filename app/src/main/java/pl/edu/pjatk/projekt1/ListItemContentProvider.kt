package pl.edu.pjatk.projekt1

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import java.lang.IllegalArgumentException

const val PROVIDER_AUTHORITY = "pl.edu.pjatk.projekt1.provider"

class ListItemContentProvider : ContentProvider() {
    private lateinit var db: ListItemDatabase;

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(PROVIDER_AUTHORITY, "item", 1)
        addURI(PROVIDER_AUTHORITY, "item/#", 2)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? = when (sUriMatcher.match(uri)) {
        1 -> "vnd.android.cursor.dir/vnd.${PROVIDER_AUTHORITY}.item"
        2 -> "vnd.android.cursor.item/vnd.${PROVIDER_AUTHORITY}.item"
        else -> throw IllegalArgumentException()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        db = Room.databaseBuilder(
            context!!,
            ListItemDatabase::class.java, "app_database"
        ).build()

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            1 -> db.query(
                SupportSQLiteQueryBuilder.builder("item")
                    .columns(projection)
                    .selection(selection, selectionArgs)
                    .orderBy(sortOrder)
                    .create()
            )
            2 -> {
                val id = ContentUris.parseId(uri)
                db.listItemDao().getById(id)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}