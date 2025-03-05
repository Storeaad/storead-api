package com.storead.profile.web.response

import com.storead.profile.application.response.ProfileServiceResponse

data class ProfileResponse(
    private val profileServiceResponse: ProfileServiceResponse,
) {
    val profileId: Long = profileServiceResponse.id
    val userId: Long = profileServiceResponse.userId
    val profileImage: String = profileServiceResponse.image.url
    val name: String = profileServiceResponse.name
    val aboutMe: String = profileServiceResponse.aboutMe


}
