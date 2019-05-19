package com.example.myscope.fragment.user

import android.app.Activity
import android.content.Intent
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
import com.example.myscope.manager.user.User
import com.example.myscope.manager.user.UserManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_personal_info.*
import java.io.File
import java.lang.ref.WeakReference
import java.util.*

class PersonalInfoFragment : ObserverFragment() {
    private var avatarUrl: String? = null
    private var bgUrl: String? = null
    private var isAvatarChanged = false
    private var isBgChanged = false
    private lateinit var user: User

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return

        val result = CropImage.getActivityResult(data)
        if (resultCode == Activity.RESULT_OK) {
            val file = File(result.uri.path)
            if (file.length() in 1 .. 15728640) {

                when (requestCode) {
                    RequestCode_Background -> {
                        val bgUri = "file://" + result.uri.path
                        ImageLoader.loadImage(img_bg, bgUri, ImageType.Background)
                        uploadImage(false, bgUri)
                    }
                    RequestCode_Avatar -> {
                        val avatarUri = "file://" + result.uri.path
                        ImageLoader.loadImage(img_avatar, avatarUri)
                        uploadImage(true, avatarUri)
                    }
                }
            } else
                Toast.makeText(mActivity, "大小超過15MB", Toast.LENGTH_SHORT).show()
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            Toast.makeText(mActivity,"裁剪失敗: ${result.error}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e("PersonalInfoFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Toast.makeText(mActivity, "個人頁面", Toast.LENGTH_SHORT).show()
        setActionBar()
        setEditStatus(false)
        setListen()

        showLoading(progressBar)
        UserManager.instance.getUserData()
    }

    private fun setActionBar() {
        mActivity.setTitle("個人頁面")
        mActivity.setBack()
        mActivity.setImageButton(2, R.drawable.edit)?.setOnClickListener {
            setEditStatus(true)
        }
    }

    private fun setListen() {
        img_bg.setOnClickListener {
            context?.let{
                val intent = CropImage.activity().setInitialCropWindowPaddingRatio(0f)
                    .setFixAspectRatio(true)
                    .setRequestedSize(1000,1500)
                    .setAspectRatio(3,2)
                startActivityForResult(intent.getIntent(it), RequestCode_Background)
            }
        }

        img_avatar.setOnClickListener {
            context?.let{
                val intent = CropImage.activity().setInitialCropWindowPaddingRatio(0f)
                    .setFixAspectRatio(true)
                    .setRequestedSize(1000,1000)
                    .setCropShape(CropImageView.CropShape.OVAL)
                startActivityForResult(intent.getIntent(it), RequestCode_Avatar)
            }
        }

        btn_save.setOnClickListener {
            val name = ed_name.text.toString()

            if (name.isNotEmpty()) {
                if (isAvatarChanged)
                    user.avatar = avatarUrl
                if (isBgChanged)
                    user.background = bgUrl

                user.name = name
                showLoading(progressBar)
                UserManager.instance.setUserData(user)
                saveLocalData(user)
            } else
                showSnackbar("資料請填寫完整")
        }
    }

    //Uplaod to Firebase Storage
    private fun uploadImage(isAvatar: Boolean, uri: String) {
        val type = if (isAvatar) StorageType.Avatar else StorageType.Background
        val name = if (isAvatar) "avatar" else "background"
        val save = WeakReference(btn_save)

        showLoading(progressBar)
        save.get()?.isEnabled = false

        //Warning: View need to use WeakReference
        StorageDatabase.instance.setStorage(uri.toUri(), type, user.uid, name) {
            msg, url ->

            hideLoading(progressBar)
            save.get()?.isEnabled = true

            if (msg == null) {
                if (isAvatar) {
                    avatarUrl = url ?: ""
                    isAvatarChanged = true
                } else {
                    bgUrl = url ?: ""
                    isBgChanged = true
                }
            } else
                showSnackbar(msg)
        }
    }

    private fun setEditStatus(editable: Boolean) {
        img_bg.isEnabled = editable
        img_avatar.isEnabled = editable
        ed_name.isEnabled = editable
        img_camera.visibility = if (editable) View.VISIBLE else View.GONE
        btn_save.visibility = if (editable) View.VISIBLE else View.GONE
    }

    private fun saveLocalData(user: User) {
        val sp = UserInfoSharedPreferences(mActivity)
        user.name?.let { sp.setName(it) }
        user.email.let { sp.setEmail(it) }
        user.avatar?.let { sp.setAvatar(it) }
        user.background?.let { sp.setBackground(it) }
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            is User -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    user = arg
                    ed_name.setText(arg.name)
                    ed_email.setText(arg.email)
                    ImageLoader.loadImage(img_avatar, arg.avatar)
                    ImageLoader.loadImage(img_bg, arg.background, ImageType.Background)
                    saveLocalData(arg)
                }
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    showSnackbar(arg.msg)
                }
            }

            Response_SetUser -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    setEditStatus(false)
                    showSnackbar("儲存完成")
                }
            }
        }
    }
}