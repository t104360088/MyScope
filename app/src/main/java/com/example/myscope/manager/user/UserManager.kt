package com.example.myscope.manager.user

import android.util.Log
import com.example.myscope.*
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.RemoteDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class UserManager : Observable() {

    //Singleton
    companion object {
        val instance: UserManager by lazy { UserManager() }
        private val mAuth by lazy { FirebaseAuth.getInstance() }
        private var mUser: User? = null
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun getMyUser(): User? {
        return mUser
    }

    fun signUp(email: String, pwd: String) {
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener {
            if (it.isComplete) {
                if (it.isSuccessful) {
                    Log.e("UserManager", "signUp : success")
                    //New user data
                    it.result?.user?.run {
                        val index = Regex("""@""").find(this.email ?: "")?.range?.start
                        val name = this.email?.substring(0, index ?: 0)
                        setUserData(User(this.uid, this.email ?: "", name = name))
                    }
                    sendEmailVerification()
                    notifyChanged(Response_SignUp)
                } else if (it.exception?.message != null) {
                    Log.e("UserManager", "signUp : fail")
                    val msg = ErrorMsg(it.exception?.message!!)
                    notifyChanged(msg)
                }
            }
        }
    }

    fun signIn(email: String, pwd: String) {
        mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener {
            if (it.isComplete) {
                when {
                    it.exception?.message != null -> {
                        Log.e("UserManager", "signIn : fail")
                        val msg = ErrorMsg(it.exception?.message!!)
                        notifyChanged(msg)
                    }
                    !it.result!!.user.isEmailVerified -> {
                        Log.e("UserManager", "signIn : account is not verify")
                        val msg = ErrorMsg("此帳號尚未完成驗證，請至信箱收取驗證信")
                        notifyChanged(msg)
                    }
                    it.isSuccessful -> {
                        Log.e("UserManager", "signIn : success")
                        notifyChanged(Response_SignIn)
                    }
                }
            }
        }
    }

    fun signOut() {
        Log.e("UserManager", "signOut")
        Log.e("UserManager", "userEmail:${getCurrentUser()?.email}")
        mAuth.signOut()
        mUser = null
    }

    fun sendEmailVerification() {
        getCurrentUser()?.sendEmailVerification()
    }

    fun sendPasswordResetEmail(email: String) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isComplete) {
                if (it.isSuccessful) {
                    Log.e("UserManager", "sendResetEmail : success")
                    sendEmailVerification()
                    notifyChanged(Response_SendEmail)
                } else if (it.exception?.message != null) {
                    Log.e("UserManager", "sendResetEmail : fail")
                    val msg = ErrorMsg(it.exception?.message!!)
                    notifyChanged(msg)
                }
            }
        }
    }

    fun setUserData(user: User) {
        getCurrentUser()?.run {
            RemoteDatabase.instance.setDocument("User", this.uid, user) {
                Log.e("UserManager", "setUserData")
                when (it) {
                    null -> notifyChanged(Response_SetUser)
                    else -> notifyChanged(ErrorMsg(it))
                }
            }
            return@run
        } ?: run {
            val msg = ErrorMsg("無法獲取此帳號資料，請重新登入")
            notifyChanged(msg)
        }
    }

    fun getUserData() {
        getCurrentUser()?.run {
            RemoteDatabase.instance.getDocument("User", this.uid) { msg, data ->
                if (!msg.isNullOrEmpty())
                    notifyChanged(ErrorMsg(msg))
                else {
                    data?.toObject(User::class.java)?.let {
                        mUser = it
                        notifyChanged(it)
                        Log.e("UserManager", "getUserData")
                    }
                }
            }
            return@run
        } ?: run {
            val msg = ErrorMsg("無法獲取此帳號資料，請重新登入")
            notifyChanged(msg)
        }
    }

    //Get others
    fun getUserData(uid: String) {
        RemoteDatabase.instance.getDocument("User", uid) { msg, data ->
            if (!msg.isNullOrEmpty())
                notifyChanged(ErrorMsg(msg))
            else {
                data?.toObject(User::class.java)?.let {
                    notifyChanged(it)
                    Log.e("UserManager", "getUserData")
                } ?: run {
                    notifyChanged(ErrorMsg("查無此人"))
                    Log.e("UserManager", "noUserData")
                }
            }
        }
    }

    fun queryUserData(email: String) {
        RemoteDatabase.instance.queryDocument("User", "email", email) {
                msg, data ->
            if (!msg.isNullOrEmpty())
                notifyChanged(ErrorMsg(msg))
            else {
                if (data?.size != 0) {
                    data!![0].toObject(User::class.java)?.let {
                        notifyChanged(it)
                        Log.e("FriendManager", "getFriendData")
                    }
                } else
                    notifyChanged(ErrorMsg("查無此人"))
            }
        }
    }

    private fun notifyChanged(res: Any?){
        setChanged()
        notifyObservers(res)
    }
}