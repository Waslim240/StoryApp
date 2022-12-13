package com.waslim.storyapp.model.response.login


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    @SerializedName("name")
    val name: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("userId")
    val userId: String?
): Parcelable