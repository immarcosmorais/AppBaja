package com.marcosmoraisDev.appbaja.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private var database: DatabaseReference? = null
private var auth: FirebaseAuth? = null
private var storage: StorageReference? = null

fun getFirebaseDatabase(): DatabaseReference {
    if (database == null) {
        database = FirebaseDatabase.getInstance().reference
    }
    return database as DatabaseReference
}

fun getFirebaseAuth(): FirebaseAuth? {
    if (auth == null){
        auth = FirebaseAuth.getInstance()
    }
    return auth
}

fun getStorageReference(): StorageReference?{
    if (storage == null){
        storage = FirebaseStorage.getInstance().reference as StorageReference
    }
    return storage
}
