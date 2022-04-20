package com.task.quixotetask

import android.app.Application
import com.task.quixotetask.database.UserDb

class UserApplication : Application() {
    val db: UserDb by lazy {
        UserDb.getInstance(this)
    }
}