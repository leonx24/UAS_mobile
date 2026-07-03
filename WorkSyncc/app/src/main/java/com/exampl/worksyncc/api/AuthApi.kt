package com.exampl.worksyncc.api

import com.exampl.worksyncc.model.LoginRequest
import com.exampl.worksyncc.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
