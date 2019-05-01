package com.example.myscope.manager

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class RemoteDatabase {

    companion object {
        val instance: RemoteDatabase by lazy { RemoteDatabase() }
        private val db by lazy { FirebaseFirestore.getInstance() }
    }

    fun setDocument(colKey: String, docKey: String, data: Any, complete: (String?) -> Unit) {
        db.collection(colKey).document(docKey).set(data).addOnCompleteListener {
                when {
                    it.exception != null -> complete(it.exception?.message)
                    it.isSuccessful -> complete(null)
                }
            }
    }

    fun getDocument(colKey: String, docKey: String, complete: (String?, DocumentSnapshot?) -> Unit) {
        db.collection(colKey).document(docKey).get().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message, null)
                it.isSuccessful -> complete(null, it.result)
            }
        }
    }
}