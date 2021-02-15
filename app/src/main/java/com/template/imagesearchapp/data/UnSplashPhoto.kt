package com.template.imagesearchapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnSplashPhoto(
    val id: String,
    val description: String?,
    val urls: UnSplashPhotoUrls,
    val user: UnSplashUser,
) : Parcelable {
    @Parcelize
    data class UnSplashPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

    @Parcelize
    data class UnSplashUser(
        val name: String,
        val username: String,
        @SerializedName("profile_image") val profileImage: ProfileImage
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"

        @Parcelize
        data class ProfileImage(
            val small: String,
            val medium: String,
            val large: String
        ) : Parcelable
    }
}