package com.example.myscope.manager

import android.util.Log
import com.example.myscope.Response_Success
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class UserManager : Observable() {

    //Singleton
    companion object {
        val instance: UserManager by lazy { UserManager() }
        private val mAuth by lazy { FirebaseAuth.getInstance() }
    }

    private fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun signUp(email: String, pwd: String) {
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener {
            if (it.isComplete) {
                if (it.isSuccessful) {
                    Log.e("signUp", "success")
                    sendEmailVerification()
                    notifyChanged(Response_Success)
                } else if (it.exception?.message != null) {
                    Log.e("signUp", "fail")
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
                        Log.e("signIn", "fail")
                        val msg = ErrorMsg(it.exception?.message!!)
                        notifyChanged(msg)
                    }
                    !it.result.user.isEmailVerified -> {
                        Log.e("signIn", "account is not verify")
                        val msg = ErrorMsg("此帳號尚未完成驗證，請至信箱收取驗證信")
                        notifyChanged(msg)
                    }
                    it.isSuccessful -> {
                        Log.e("signIn", "success")
                        notifyChanged(Response_Success)
                    }
                }
            }
        }
    }

    fun sendEmailVerification() {
        getUser()?.sendEmailVerification()
    }

    private fun notifyChanged(res: Any){
        setChanged()
        notifyObservers(res)
    }
}