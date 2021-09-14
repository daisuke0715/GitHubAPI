package com.daisukekawamura.gitapiapp

import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): UserData
}