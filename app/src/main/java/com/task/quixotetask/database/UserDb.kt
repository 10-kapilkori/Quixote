package com.task.quixotetask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.task.quixotetask.Notes
import com.task.quixotetask.Users

@Database(entities = [Users::class, Notes::class], exportSchema = true, version = 2)
abstract class UserDb : RoomDatabase() {
    abstract fun getDao(): DAO

    companion object {
        @Volatile
        private var INSTANCE: UserDb? = null

        fun getInstance(context: Context): UserDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(context, UserDb::class.java, "User_DB")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}