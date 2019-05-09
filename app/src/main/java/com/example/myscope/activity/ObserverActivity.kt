package com.example.myscope.activity

import android.os.Bundle
import com.example.myscope.manager.user.UserManager
import java.util.*

abstract class ObserverActivity : BaseActivity(), Observer {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserManager.instance.addObserver(this)
    }

    override fun onResume() {
        super.onResume()
        UserManager.instance.addObserver(this)
    }

    override fun onStop() {
        super.onStop()
        UserManager.instance.deleteObserver(this)
    }
}