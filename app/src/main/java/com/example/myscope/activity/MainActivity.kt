package com.example.myscope.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myscope.fragment.LoginFragment
import com.example.myscope.R
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchTo(this, LoginFragment())
    }

    fun switchTo(activity: AppCompatActivity, fragment : Fragment, bundle: Bundle? = null, broken: Boolean = false){
        val fm = activity.supportFragmentManager
        if(broken || fm.findFragmentByTag(fragment.javaClass.simpleName)==null){
            if(broken && fm.backStackEntryCount>0)
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            Log.e("switchTo", fragment.javaClass.simpleName)
            fragment.arguments = bundle
            val ft = fm.beginTransaction()
            ft.setCustomAnimations(
                R.anim.abc_fade_in,
                R.anim.abc_fade_out,
                R.anim.abc_grow_fade_in_from_bottom,
                R.anim.abc_shrink_fade_out_from_bottom
            )
            ft.replace(R.id.fl_fragment, fragment, fragment.javaClass.simpleName)
            ft.addToBackStack(fragment.javaClass.simpleName)
            ft.commit()
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
