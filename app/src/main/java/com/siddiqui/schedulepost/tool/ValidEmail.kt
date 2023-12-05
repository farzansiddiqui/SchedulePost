package com.siddiqui.schedulepost.tool

class ValidEmail {
     fun isValidEmail(email: String): Boolean {
        val regex = Regex("^(([\\w-.]+)@([\\w-]+\\.)+([a-z]{2,}))")
        return regex.matches(email)

    }
}