package ter.den.feature_home.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ter.den.feature_home.data.db.model.PopularDb

@Dao
interface PopularDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<PopularDb>)

    @Query("SELECT * FROM ${PopularDb.TABLE_NAME}")
    suspend fun getPopular(): List<PopularDb>

}