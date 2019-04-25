package com.example.myscope.fragment.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myscope.R
import com.example.myscope.activity.BaseActivity

abstract class BaseFragment : Fragment() {
    lateinit var mActivity: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        mActivity = activity as BaseActivity
        super.onCreate(savedInstanceState)
    }

    fun switchTo(fragment : Fragment, bundle: Bundle? = null, broken: Boolean = false) {
        val fm = mActivity.supportFragmentManager
        if(broken || fm.findFragmentByTag(fragment.javaClass.simpleName) == null){
            if(broken && fm.backStackEntryCount > 0) {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                Log.e("switchTo", "清空堆疊")
            }

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
            ft.addToBackStack(fragment.javaClass.simpleName) //Add stack
            ft.commit()
        }
    }

    //Back to last view
    fun popBack() {
        mActivity.onBackPressed()
    }

    //Back to first fragment
    fun popBackFirst(){
        val fm = mActivity.supportFragmentManager

        if (fm.backStackEntryCount > 0)
            fm.popBackStack(fm.getBackStackEntryAt(1).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    //Exit all fragment
    fun popBackAll(){
        val fm = mActivity.supportFragmentManager

        if (fm.backStackEntryCount > 0)
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun showKeyBoard(view: EditText) {
        val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    fun hideKeyBoard() {
        mActivity.currentFocus?.let {
            val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

//    fun hideKeyBoard(context: Context, view: View) {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }

    fun showLoading(view: ProgressBar) {
        view.visibility = View.VISIBLE
    }

    fun hideLoading(view: ProgressBar) {
        view.visibility = View.GONE
    }
}