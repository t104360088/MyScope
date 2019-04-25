package com.example.myscope.manager.user

import android.util.Log
import com.example.myscope.Response_Success
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
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun signUp(email: String, pwd: String) {
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener {
            if (it.isComplete) {
                if (it.isSuccessful) {
                    Log.e("UserManager", "signUp : success")
                    sendEmailVerification()
                    notifyChanged(Response_Success)
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
                        notifyChanged(Response_Success)
                    }
                }
            }
        }
    }

    fun signOut() {
        Log.e("UserManager", "signOut")
        Log.e("UserManager", "userEmail:${getCurrentUser()?.email}")
        mAuth.signOut()
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
                    notifyChanged(Response_Success)
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
                when (it) {
                    null -> notifyChanged(Response_Success)
                    else -> notifyChanged(ErrorMsg(it))
                }
            }
            return@run
        }
        val msg = ErrorMsg("無法獲取此帳號資料，請重新登入")
        notifyChanged(msg)
    }

    private fun notifyChanged(res: Any){
        setChanged()
        notifyObservers(res)
    }
}