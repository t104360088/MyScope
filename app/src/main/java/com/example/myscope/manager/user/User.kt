package com.example.myscope.manager.user

data class User (val uid: String = "",
                 val email: String = "",
                 val onlineTime: Long = 0,
                 val isOnline: Boolean = false,
                 val name: String? = null,
                 val avatar: String? = null)