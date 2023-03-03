package ter.den.feature_profile.data.network.model.account

import ter.den.feature_profile.domain.model.AccountDetails
import ter.den.feature_profile.domain.model.Avatar

data class AccountDetailsDto(
    val avatar: Avatar,
    val id: Int,
    val include_adult: Boolean,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val username: String
)

fun AccountDetailsDto.toAccountDetails() = AccountDetails(
    avatar, id, include_adult, iso_3166_1, iso_639_1, name, username
)