package ter.den.feature_home.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ter.den.feature_home.data.db.model.PopularDb

@Database(entities = [PopularDb::class], version = 1, exportSchema = false)
internal abstract class PopularDatabase : RoomDatabase() {
    abstract fun popularDao(): PopularDao

    companion object {
        private const val POPULAR_DB_NAME = "popular_db"
        fun getInstance(context: Context): PopularDao =
            Room.databaseBuilder(context, PopularDatabase::class.java, POPULAR_DB_NAME)
                .build()
                .popularDao()
    }
}