package com.siddiqui.schedulepost.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserRegistration(val name:String? = null,
    val emailOrMobile:String?= null, val password:String? = null)

