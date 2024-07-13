package com.android.chatnappsummers.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.chatnappsummers.models.User
import com.android.chatnappsummers.repository.SignUpCredentialsRepository
import com.google.firebase.firestore.FirebaseFirestore

class SignUpCredentialsViewModel(val app : Application) : AndroidViewModel(app)
{
    private val repo : SignUpCredentialsRepository by lazy { SignUpCredentialsRepository(app) }
    private val firestore : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun storeUserInCloud(user : User)
    {

    }


}