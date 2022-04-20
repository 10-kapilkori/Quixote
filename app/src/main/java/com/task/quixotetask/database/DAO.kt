package com.task.quixotetask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.task.quixotetask.Notes
import com.task.quixotetask.Users

@Dao
interface DAO {
    @Insert
    suspend fun insert(user: Users)

    @Insert
    suspend fun insertNote(notes: Notes)

    @Query("SELECT * FROM user")
    fun fetchUsers(): LiveData<List<Users>>

    @Query("SELECT * FROM notes")
    fun fetchAllNotes(): LiveData<List<Notes>>

    @Query("SELECT * FROM user WHERE email=:email AND password=:password")
    fun fetchUserEmail(email: String, password: String): LiveData<Users>

    @Query("SELECT * FROM user WHERE number=:phone AND password=:password")
    fun fetchUserPhone(phone: String, password: String): LiveData<Users>

    @Query("SELECT * FROM notes WHERE email=:email")
    fun fetchUserNotes(email: String): LiveData<List<Notes>>
}
