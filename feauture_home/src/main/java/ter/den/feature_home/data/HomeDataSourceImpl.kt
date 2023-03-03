package ter.den.feature_home.data

import ter.den.feature_home.data.db.PopularDao
import ter.den.feature_home.data.db.model.PopularDb
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val dao: PopularDao
) : HomeDataSource {

    override suspend fun getPopular(): List<PopularDb> = dao.getPopular()

}