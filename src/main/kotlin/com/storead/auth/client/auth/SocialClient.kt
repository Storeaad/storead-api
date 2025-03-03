package com.storead.auth.client.auth

import com.storead.auth.application.request.AuthServiceRequest

interface SocialClient {

    fun getPlatformUserInfoByAccessToken(accessToken: String): AuthServiceRequest

}