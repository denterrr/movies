package ter.den.feature_home.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PopularDb.TABLE_NAME)
data class PopularDb(
    @ColumnInfo
    val adult: Boolean,
    @ColumnInfo
    val backdrop_path: String,
    @ColumnInfo
    val genre_ids: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo
    val id: Int,
    @ColumnInfo
    val original_language: String,
    @ColumnInfo
    val original_title: String,
    @ColumnInfo
    val overview: String,
    @ColumnInfo
    val popularity: Double,
    @ColumnInfo
    val poster_path: String,
    @ColumnInfo
    val release_date: String,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val video: Boolean,
    @ColumnInfo
    val vote_average: Double,
    @ColumnInfo
    val vote_count: Int
){
    companion object {
        const val TABLE_NAME = "popular_table"
    }
}