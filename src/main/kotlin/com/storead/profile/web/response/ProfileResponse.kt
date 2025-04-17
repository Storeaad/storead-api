package com.storead.profile.web.response

import com.storead.profile.application.response.ProfileServiceResponse
import java.util.UUID

data class ProfileResponse(
    private val profileServiceResponse: ProfileServiceResponse,
) {
    val profileId: UUID = profileServiceResponse.id
    val userId: UUID = profileServiceResponse.userId
    val profileImage: String = profileServiceResponse.image.url
    val name: String = profileServiceResponse.name
    val aboutMe: String = profileServiceResponse.aboutMe


}
