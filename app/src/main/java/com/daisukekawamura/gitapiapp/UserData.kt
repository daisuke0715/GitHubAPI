package com.daisukekawamura.gitapiapp

import com.google.gson.annotations.SerializedName

data class UserData(
    val name: String,
    @SerializedName("login") val userId: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    val following: Int,
    val followers: Int
)