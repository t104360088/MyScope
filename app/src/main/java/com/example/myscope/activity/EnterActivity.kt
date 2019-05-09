package com.example.myscope.activity

import android.os.Bundle
import android.util.Log
import com.example.myscope.R
import com.example.myscope.fragment.user.EnterFragment

class EnterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        //Switch to EnterFragment
        Log.e("EnterActivity", "onCreate")
        val fm = this.supportFragmentManager
        val ft = fm.beginTransaction()
        val fragment = EnterFragment()
        ft.add(R.id.fl_fragment, fragment, fragment.javaClass.simpleName)
        ft.commit()
    }

    override fun onBackPressed() {
        val count = this.supportFragmentManager.backStackEntryCount
        Log.e("EnterActivity", "stackCount:$count")
        //The first fragment is EnterFragment
        if (count < 2)
            finish()
        else
            super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("EnterActivity", "onDestroy")
    }
}
