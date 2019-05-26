package com.example.myscope.manager.article

import java.io.Serializable

data class Article (val id: String = "",
                    val uid: String = "",
                    val name: String = "",
                    val avatar: String = "",
                    var content: String = "",
                    var photo: String = "",
                    var privacy: Int = -1,
                    var tag: List<Tag> = listOf(),
                    var timestamp: Long = 0): Serializable

data class ArticleList(val list: Array<Article?> = arrayOf())

data class Tag(val uid: String, val name: String)