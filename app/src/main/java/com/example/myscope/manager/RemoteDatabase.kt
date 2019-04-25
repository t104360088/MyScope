package com.example.myscope.manager

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
}