package com.example.myscope.fragment.base

import android.os.Bundle
import android.util.Log
import com.example.myscope.manager.chat.ChatManager
import com.example.myscope.manager.friend.FriendManager
import com.example.myscope.manager.user.UserManager
import java.util.*

/***
 * ViewPager的預先載入跟一般fragment生命週期不同
 * 預先載入都會走完一般fragment初始化的生命週期
 * 使觀察者模式出現問題，viewpager未呈現的fragment網路資源會被載入
 * 因此在viewPager中使用觀察者者必須繼承此類別(LazyLoadFragment)
 */
abstract class LazyLoadFragment : BaseFragment(), Observer {
    protected var isViewInitiated: Boolean = false
    protected var isDataLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewInitiated = true
        prepareRequestData() //好像用不到，考慮拿掉
    }

    //在Fragment畫面消失、顯示在user面前時調用
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        prepareRequestData(isVisibleToUser)
        //prepareRequestData() 原本使用懶加載，但資料要更新，所以改成上面寫法
    }

    //在此實作被要求的網路資源
    abstract fun requestData()

    @JvmOverloads
    fun prepareRequestData(forceUpdate: Boolean = false): Boolean {
        if (userVisibleHint && isViewInitiated && (!isDataLoaded || forceUpdate)) {
            Log.e("LazyLoadFragment", "取得網路資源")
            isDataLoaded = true
            UserManager.instance.addObserver(this)
            FriendManager.instance.addObserver(this)
            ChatManager.instance.addObserver(this)
            requestData()
            return true
        }
        UserManager.instance.deleteObserver(this)
        FriendManager.instance.deleteObserver(this)
        ChatManager.instance.addObserver(this)
        return false
    }

    //因切換至其他fragment(指非viewPager，即switchTo)不會先執行setUserVisibleHint，故在此刪除觀察者
    override fun onStop() {
        super.onStop()
        UserManager.instance.deleteObserver(this)
        FriendManager.instance.deleteObserver(this)
        ChatManager.instance.deleteObserver(this)
    }
}