package com.example.myscope.manager.user

data class User (val uid: String = "",
                 val email: String = "",
                 var onlineTime: Long = 0,
                 var name: String? = null,
                 var avatar: String? = null,
                 var background: String? = null)