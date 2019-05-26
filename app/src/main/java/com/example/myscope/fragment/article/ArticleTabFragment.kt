package com.example.myscope.fragment.article

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.RequestCode_Article
import com.example.myscope.Response_DeleteArticle
import com.example.myscope.activity.MainActivity
import com.example.myscope.adapter.ArticleAdapter
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.article.Article
import com.example.myscope.manager.article.ArticleList
import com.example.myscope.manager.article.ArticleManager
import com.example.myscope.manager.user.UserManager
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_article.*
import java.io.File
import java.util.*

class ArticleTabFragment : ObserverFragment() {
    private lateinit var adapter: ArticleAdapter
    private val articles = arrayListOf<Article>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data==null) return

        when(requestCode){
            RequestCode_Article ->{
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val file = File(result.uri.path)
                    if (file.length() in 1 .. 15728640) {
                        val b = Bundle()
                        b.putBoolean("IsPost", true)
                        b.putString("Uri", result.uri.toString())
                        switchTo(PostArticleFragment(), b)
                        (mActivity as MainActivity).showNavigationDrawer(false)
                        (mActivity as MainActivity).showNavigationBottom(false)
                    } else
                        Toast.makeText(mActivity, "檔案大小超過 15 MB", Toast.LENGTH_SHORT).show()
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                    Toast.makeText(mActivity,"裁剪失敗: ${result.error}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initListView()
        setActionBar()
        setListen()
        //UserManager.instance.setUserData()
        //setPage(1, 2)
        ArticleManager.instance.getArticleList()
    }

    private fun initListView() {
        UserManager.instance.getMyUser()?.let {
            if (!::adapter.isInitialized){
                adapter = ArticleAdapter(mActivity, articles, it.uid)
            }

            listView.adapter = adapter
        }
    }

    private fun setActionBar() {
        mActivity.setTitle("貼文")
        (mActivity as MainActivity).showNavigationDrawer()
        (mActivity as MainActivity).showNavigationBottom()
    }

    private fun setListen() {
        fab_add.setOnClickListener {
            context?.let{
                val intent = CropImage.activity().setInitialCropWindowPaddingRatio(0f)
                    .setFixAspectRatio(true).setRequestedSize(1000,1000)
                startActivityForResult(intent.getIntent(it), RequestCode_Article)
            }
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            is ArticleList -> {
                mActivity.runOnUiThread {
                    articles.clear()
                    for (i in arg.list)
                        articles.add(i!!)
                    adapter.notifyDataSetChanged()
                }
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    showSnackbar(arg.msg)
                }
            }
            Response_DeleteArticle -> {
                ArticleManager.instance.getArticleList()
            }
        }
    }
}