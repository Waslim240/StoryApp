package com.waslim.storyapp.model.response.story


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAllStoryResponse(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("listStory")
    val listStory: List<Story>?,
    @SerializedName("message")
    val message: String?
): Parcelable