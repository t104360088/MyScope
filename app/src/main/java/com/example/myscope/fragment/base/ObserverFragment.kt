package com.example.myscope.fragment.base

import android.os.Bundle
import com.example.myscope.manager.article.ArticleManager
import com.example.myscope.manager.chat.ChatManager
import com.example.myscope.manager.friend.FriendManager
import com.example.myscope.manager.user.UserManager
import java.util.*

abstract class ObserverFragment : BaseFragment(), Observer {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UserManager.instance.addObserver(this)
        FriendManager.instance.addObserver(this)
        ArticleManager.instance.addObserver(this)
        ChatManager.instance.addObserver(this)
    }

    override fun onResume() {
        super.onResume()
        UserManager.instance.addObserver(this)
        FriendManager.instance.addObserver(this)
        ArticleManager.instance.addObserver(this)
        ChatManager.instance.addObserver(this)
    }

    override fun onStop() {
        super.onStop()
        UserManager.instance.deleteObserver(this)
        FriendManager.instance.deleteObserver(this)
        ArticleManager.instance.deleteObserver(this)
        ChatManager.instance.deleteObserver(this)
    }
}