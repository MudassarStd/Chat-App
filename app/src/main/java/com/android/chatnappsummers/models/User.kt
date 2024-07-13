package com.android.chatnappsummers.models

import com.google.firebase.Timestamp

data class User(
    val name : String,
    val phone : String,
    val photo : String,
    val registrationStatus : Boolean,
    val dateRegistration : Timestamp = Timestamp.now()
)
