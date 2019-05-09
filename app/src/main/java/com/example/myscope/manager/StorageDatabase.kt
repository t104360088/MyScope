package com.example.myscope.manager

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

class StorageDatabase {

    companion object {
        val instance: StorageDatabase by lazy { StorageDatabase() }
        private val db by lazy { FirebaseStorage.getInstance() }
    }

    fun setStorage(uri: Uri, type: StorageType, uid: String,
                   fileName: String, complete: (String?, String?) -> Unit) {

        val folder = when (type) {
            StorageType.Avatar -> "avatar"
            StorageType.Background -> "background"
            StorageType.Article -> "article"
        }
        val path = "$uid/$folder/$fileName"

        db.reference.child(path).putFile(uri).addOnCompleteListener {
            if (it.isSuccessful) {
                db.reference.child(path).downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.e("Storage", "successful")
                        complete(null, it.result.toString())
                    } else {
                        Log.e("Storage", "downloadUrl fail")
                        complete(it.exception?.message, null)
                    }
                }
            } else {
                Log.e("Storage", "uplaod fail")
                complete(it.exception?.message, null)
            }
        }
    }
}

enum class StorageType {
    Avatar,
    Background,
    Article
}