package ter.den.feature_home.data

import ter.den.feature_home.data.db.model.PopularDb

interface HomeDataSource {

    suspend fun getPopular(): List<PopularDb>

}