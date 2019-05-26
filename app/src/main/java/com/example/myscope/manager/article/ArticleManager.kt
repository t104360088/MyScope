package com.example.myscope.manager.article

import android.util.Log
import com.example.myscope.Response_DeleteArticle
import com.example.myscope.Response_SetArticle
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.RemoteDatabase
import java.util.*

class ArticleManager : Observable() {

    //Singleton
    companion object {
        val instance: ArticleManager by lazy { ArticleManager() }
    }

    fun getAutoID(): String {
        return RemoteDatabase.instance.getAutoID("Article")
    }

    fun setArticleData(article: Article) {
        RemoteDatabase.instance.setDocument("Article", article.id, article) {
            Log.e("ArticleManager", "setArticleData")
            when (it) {
                null -> notifyChanged(Response_SetArticle)
                else -> notifyChanged(ErrorMsg(it))
            }
        }
    }

    fun getArticleList() {
        RemoteDatabase.instance.getDocumentList("Article", 10) {
                msg, list ->
            if (msg != null) {
                notifyChanged(ErrorMsg(msg))
            } else {
                val mList = arrayListOf<Article?>()
                list?.listIterator()?.forEach {
                    mList.add(it.toObject(Article::class.java))
                }
                notifyChanged(ArticleList(mList.toTypedArray()))
                Log.e("ArticleManager", "getArticleList")
            }
        }
    }

    fun deleteArticle(articleID: String) {
        RemoteDatabase.instance.deleteDocument("Article", articleID) {
            Log.e("ArticleManager", "deleteArticle")
            when (it) {
                null -> notifyChanged(Response_DeleteArticle)
                else -> notifyChanged(ErrorMsg(it))
            }
        }
    }

    private fun notifyChanged(res: Any?){
        setChanged()
        notifyObservers(res)
    }
}