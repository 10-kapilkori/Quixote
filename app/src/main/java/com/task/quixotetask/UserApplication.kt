package com.task.quixotetask

import android.app.Application

class UserApplication : Application() {
    val db: UserDb by lazy {
        UserDb.getInstance(this)
    }
}