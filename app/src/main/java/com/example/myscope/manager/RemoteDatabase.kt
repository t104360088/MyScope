package com.example.myscope.manager

import com.google.firebase.firestore.*

class RemoteDatabase {

    companion object {
        val instance: RemoteDatabase by lazy { RemoteDatabase() }
        private val db by lazy { FirebaseFirestore.getInstance() }
    }

    fun getAutoID(colKey: String, docKey: String? = null, colKey2: String? = null): String {
        return if (docKey != null && colKey2 != null)
            db.collection(colKey).document(docKey).collection(colKey2).document().id
        else
            db.collection(colKey).document().id
    }

    //One layer
    fun setDocument(colKey: String, docKey: String, data: Any, complete: (String?) -> Unit) {
        db.collection(colKey).document(docKey).set(data).addOnCompleteListener {
                when {
                    it.exception != null -> complete(it.exception?.message)
                    it.isSuccessful -> complete(null)
                }
            }
    }

    //Two layers
    fun setDocument(colKey: String, docKey: String,
                    colKey2: String, docKey2: String, data: Any, complete: (String?) -> Unit) {
        db.collection(colKey).document(docKey)
            .collection(colKey2).document(docKey2).set(data).addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message)
                it.isSuccessful -> complete(null)
            }
        }
    }

    //One layer
    fun getDocument(colKey: String, docKey: String, complete: (String?, DocumentSnapshot?) -> Unit) {
        db.collection(colKey).document(docKey).get().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message, null)
                it.isSuccessful -> complete(null, it.result)
            }
        }
    }

    //Two layers
    fun getDocument(colKey: String, docKey: String,
                    colKey2: String, docKey2: String, complete: (String?, DocumentSnapshot?) -> Unit) {
        db.collection(colKey).document(docKey).collection(colKey2).document(docKey2).get().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message, null)
                it.isSuccessful -> complete(null, it.result)
            }
        }
    }

    //One layer
    fun getDocumentList(colKey: String, limit: Long, complete: (String?, List<DocumentSnapshot>?) -> Unit) {
        db.collection(colKey).limit(limit).get().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message, null)
                it.isSuccessful -> complete(null, it.result?.documents)
            }
        }
    }

    //Two layers
    fun getDocumentList(colKey: String, docKey: String, colKey2: String, limit: Long, complete: (String?, List<DocumentSnapshot>?) -> Unit) {
        db.collection(colKey).document(docKey).collection(colKey2).limit(limit).get().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message, null)
                it.isSuccessful -> complete(null, it.result?.documents)
            }
        }
    }

    fun getRealtimeDocumentList(colKey: String, docKey: String, colKey2: String, orderBy: String, complete: (String?, List<DocumentSnapshot>?) -> Unit): ListenerRegistration {
        val docRef = db.collection(colKey).document(docKey).collection(colKey2).orderBy(orderBy)
        return docRef.addSnapshotListener(EventListener<QuerySnapshot> { snapshot, e ->
            if (e != null) {
                complete(e.message, null)
                return@EventListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                complete(null, snapshot.documents)
            } else {
                //complete("Current data: null", null)
            }
        })
    }

    //One layer
    fun deleteDocument(colKey: String, docKey: String, complete: (String?) -> Unit) {
        db.collection(colKey).document(docKey).delete().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message)
                it.isSuccessful -> complete(null)
            }
        }
    }

    //Two layers
    fun deleteDocument(colKey: String, docKey: String, colKey2: String, docKey2: String, complete: (String?) -> Unit) {
        db.collection(colKey).document(docKey).collection(colKey2).document(docKey2).delete().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message)
                it.isSuccessful -> complete(null)
            }
        }
    }

    //One layer
    fun queryDocument(colKey: String, queryKey: String, queryValue: Any, complete: (String?, List<DocumentSnapshot>?) -> Unit) {
        db.collection(colKey).whereEqualTo(queryKey, queryValue).get().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message, null)
                it.isSuccessful -> complete(null, it.result?.documents)
            }
        }
    }

    //Two layers
    fun queryDocument(colKey: String, docKey: String, colKey2: String,
                      queryKey: String, queryValue: Any, complete: (String?, List<DocumentSnapshot>?) -> Unit) {
        db.collection(colKey).document(docKey).collection(colKey2).whereEqualTo(queryKey, queryValue).get().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message, null)
                it.isSuccessful -> complete(null, it.result?.documents)
            }
        }
    }

    //Query one key & one value
    fun queryDocumentListByEqualTo(colKey: String, docKey: String, colKey2: String, queryKey: String, queryValue: Any,
                          limit: Long, complete: (String?, List<DocumentSnapshot>?) -> Unit) {
        db.collection(colKey).document(docKey).collection(colKey2)
            .whereEqualTo(queryKey, queryValue)
            .limit(limit).get().addOnCompleteListener {
                when {
                    it.exception != null -> complete(it.exception?.message, null)
                    it.isSuccessful -> complete(null, it.result?.documents)
                }
            }
    }

    //Query one key & two value
    fun queryDocumentListByLessThan(colKey: String, docKey: String, colKey2: String, queryKey: String, queryValue: Any,
                          limit: Long, complete: (String?, List<DocumentSnapshot>?) -> Unit) {
        db.collection(colKey).document(docKey).collection(colKey2)
            .whereLessThan(queryKey, queryValue)
            .limit(limit).get().addOnCompleteListener {
            when {
                it.exception != null -> complete(it.exception?.message, null)
                it.isSuccessful -> complete(null, it.result?.documents)
            }
        }
    }
}