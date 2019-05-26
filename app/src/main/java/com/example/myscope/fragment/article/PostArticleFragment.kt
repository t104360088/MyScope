package com.example.myscope.fragment.article

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.example.myscope.*
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.StorageDatabase
import com.example.myscope.manager.StorageType
import com.example.myscope.manager.article.Article
import com.example.myscope.manager.article.ArticleManager
import com.example.myscope.manager.article.Tag
import com.example.myscope.manager.friend.Friend
import com.example.myscope.manager.user.User
import com.example.myscope.manager.user.UserManager
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_post_article.*
import java.io.File
import java.util.*

class PostArticleFragment : ObserverFragment() {
    private lateinit var uri: String
    private lateinit var url: String
    private lateinit var article: Article
    private lateinit var id: String
    private lateinit var user: User

    private var isPost = true
    private var isImageChanged = true
    private var name = ""
    private var tagList = arrayListOf<Tag>()
    private var privacy = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data==null) return

        when(requestCode){
            RequestCode_Article ->{
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val file = File(result.uri.path)
                    if (file.length() in 1 .. 15728640) {
                        isImageChanged = true
                        uri = "file://" + result.uri.path
                        imageView.setImageURI(Uri.parse(uri))
                    } else
                        Toast.makeText(mActivity, "檔案大小超過 15 MB", Toast.LENGTH_SHORT).show()
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                    Toast.makeText(mActivity,"裁剪失敗: ${result.error}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            isPost = it.getBoolean("IsPost")


            if (isPost)
                uri = it.getString("Uri")
            else {
                isImageChanged = false
                article = it.getSerializable("Article") as Article
                id = article.id
                privacy = article.privacy
                url = article.photo

                if (article.tag.isNotEmpty()) {
                    val tag = Tag(article.tag[0].uid, article.tag[0].name)
                    name = article.tag[0].name
                    tagList.add(tag)

                    for (i in 1 until article.tag.size) {
                        val tags = Tag(article.tag[i].uid, article.tag[i].name)
                        name += ", " + article.tag[i].name
                        tagList.add(tags)
                    }
                }
            }
        }

        UserManager.instance.getMyUser()?.let {
            user = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setActionBar()
        return inflater.inflate(R.layout.fragment_post_article, container, false)
    }

    private fun setActionBar(){
        val title = if (isPost) "發布貼文" else "編輯貼文"
        mActivity.setTitle(title)
        mActivity.setBack(true)
        mActivity.setImageButton(2, R.drawable.export)?.setOnClickListener {
            if (isImageChanged)
                uploadImage()
            else
                post()
        }
    }

    //Upload to Firebase Storage
    private fun uploadImage() {
        showLoading(progressBar)

        //Warning: View need to use WeakReference
        id = if (isPost) ArticleManager.instance.getAutoID() else article.id
        StorageDatabase.instance.setStorage(uri.toUri(), StorageType.Article, user.uid, id) {
                msg, url ->

            hideLoading(progressBar)
            Log.e("PostArticleFragment", "uploadImage: success")
            if (msg == null && url != null) {
                this.url = url
                post()
            } else if (msg != null)
                showSnackbar(msg)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isPost)
            imageView.setImageURI(Uri.parse(uri))
        else {
            ImageLoader.loadImage(imageView, article.photo, ImageType.Article)
            ed_content.setText(article.content)
            tv_tag.text = name
        }

        initTextDrawable()
        setListen()
    }

    private fun initTextDrawable(){
        var image = resources.getDrawable(R.drawable.world, mActivity.theme)
        image.mutate()
        image.setBounds(0, 0, image.intrinsicHeight, image.intrinsicHeight)
        image.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        tv_public.setCompoundDrawables(image, null, null, null)
        image = resources.getDrawable(R.drawable.friend, mActivity.theme)
        image.mutate()
        image.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        image.setBounds(0, 0, image.intrinsicHeight, image.intrinsicHeight)
        tv_friend.setCompoundDrawables(image, null, null, null)
        image = resources.getDrawable(R.drawable.lock, mActivity.theme)
        image.mutate()
        image.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        image.setBounds(0, 0, image.intrinsicHeight, image.intrinsicHeight)
        tv_private.setCompoundDrawables(image, null, null, null)

        setColor()
    }

    private fun setColor() {
        tv_public.setBackgroundResource(R.drawable.rectangle_black)
        tv_friend.setBackgroundResource(R.drawable.rectangle_black)
        tv_private.setBackgroundResource(R.drawable.rectangle_black)

        when (privacy) {
            0 -> tv_public.setBackgroundResource(R.drawable.rectangle_blue)
            1 -> tv_friend.setBackgroundResource(R.drawable.rectangle_blue)
            2 -> tv_private.setBackgroundResource(R.drawable.rectangle_blue)
        }
    }

    private fun setListen() {
        tv_public.setOnClickListener {
            privacy = 0
            setColor()
        }

        tv_friend.setOnClickListener {
            privacy = 1
            setColor()
        }

        tv_private.setOnClickListener {
            privacy = 2
            setColor()
        }

        tv_tag.setOnClickListener {
            val b = Bundle()
            b.putInt("Mode", 1)
            //Method.switchTo(mActivity, SocialListFragment(), b)
        }

        imageView.setOnClickListener {
            context?.let{
                val intent = CropImage.activity().setInitialCropWindowPaddingRatio(0f)
                    .setFixAspectRatio(true).setRequestedSize(1000,1000)
                startActivityForResult(intent.getIntent(it), RequestCode_Article)
            }
        }
    }

    fun setTag(friend: ArrayList<Friend>) {
        tagList.clear()

        if (friend.size > 0) {
            val tag = Tag(friend[0].uid, friend[0].name)
            name = friend[0].name
            tagList.add(tag)

            for (i in 1 until friend.size) {
                val tags = Tag(friend[i].uid, friend[i].name)
                name += ", " + friend[i].name
                tagList.add(tags)
            }
        } else
            name = ""
    }

    private fun post() {
        Log.e("PostArticleFragment", "post")
        val content = ed_content.text.toString()
        val timestamp = System.currentTimeMillis()
        val article = Article(id, user.uid, user.name ?: "", user.avatar ?: "",
                            content, url, privacy, tagList, timestamp)

        showLoading(progressBar)
        ArticleManager.instance.setArticleData(article)
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            Response_SetArticle -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    popBack()
                }
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    showSnackbar(arg.msg)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyBoard()
    }
}