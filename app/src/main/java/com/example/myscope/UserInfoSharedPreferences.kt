package com.example.myscope

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class UserInfoSharedPreferences(context: Context) {
    companion object {
        private const val UserID = "UserID"
        private const val Name = "Name"
        private const val Gender = "Gender"
        private const val Photo = "Photo"
        private const val Email = "Email"
        private const val Password = "Password"
        private const val Avatar = "Avatar"
        private const val Background = "Background"
        private const val LoginType = "LoginType"
        private const val Language = "Language"
    }

    private val sp: SharedPreferences =
        context.getSharedPreferences("MyScope", Context.MODE_PRIVATE)

    fun clear() = sp.edit().clear().apply()

    fun clearLoginData() {
        sp.edit().remove(Email).apply()
        sp.edit().remove(Password).apply()
    }

    fun setUserID(id: String) = sp.edit().putString(UserID, id).apply()

    fun setName(name: String) = sp.edit().putString(Name, name).apply()

    fun setGender(gender: Int) = sp.edit().putInt(Gender, gender).apply()

    fun setPhoto(photo: String) = sp.edit().putString(Photo, photo).apply()

    fun setEmail(email: String) = sp.edit().putString(Email, email).apply()

    fun setPassword(pwd: String) = sp.edit().putString(Password, pwd).apply()

    fun setAvatar(url: String) = sp.edit().putString(Avatar, url).apply()

    fun setBackground(url: String) = sp.edit().putString(Background, url).apply()

    fun setLoginType(type: Int) = sp.edit().putInt(LoginType, type).apply()

    fun setLanguage(language: String) = sp.edit().putString(Language, language).apply()

    fun getUserID(): String = sp.getString(UserID,"")

    fun getName(): String = sp.getString(Name,"")

    fun getGender(): Int = sp.getInt(Gender,1)

    fun getPhoto(): String = sp.getString(Photo,"")

    fun getEmail(): String = sp.getString(Email,"")

    fun getPassword(): String = sp.getString(Password, "")

    fun getAvatar(): String = sp.getString(Avatar, "")

    fun getBackground(): String = sp.getString(Background, "")

    fun getLoginType(): Int = sp.getInt(LoginType,2)

    fun getLanguage(): String = sp.getString(Language, Locale.getDefault().language)
}