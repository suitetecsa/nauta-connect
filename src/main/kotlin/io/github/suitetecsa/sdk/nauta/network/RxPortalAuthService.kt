package io.github.suitetecsa.sdk.nauta.network

import io.github.suitetecsa.sdk.nauta.model.captcha.CaptchaResponse
import io.github.suitetecsa.sdk.nauta.model.login.LoginRequest
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponse
import io.github.suitetecsa.sdk.nauta.model.users.UsersRequest
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface RxPortalAuthService {
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @GET("/captcha/captcha?")
    fun getCaptcha(): Single<CaptchaResponse>

    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Single<LoginResponse>


    @Headers(
        "User-Agent: SuitETECSA/1.0.0",
        "Content-Type: application/json",
        "usernameApp: portal",
    )
    @POST("/users")
    fun users(
        @Header("Authorization") authorization: String,
        @Header("passwordApp") passwordApp: String,
        @Body userRequest: UsersRequest
    ): Single<UsersResponse>
}
