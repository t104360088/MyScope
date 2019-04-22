package com.example.myscope.fragment

import android.os.Bundle
import com.example.myscope.manager.UserManager
import java.util.*

abstract class ObserverFragment : BaseFragment(), Observer {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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