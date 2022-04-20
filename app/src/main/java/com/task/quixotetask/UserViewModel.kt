package com.task.quixotetask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val TAG = "UserViewModel"

class UserViewModel(private var dao: DAO) : ViewModel() {
    fun insertUser(users: Users) {
        viewModelScope.launch {
            dao.insert(users)
        }
    }

    fun insertNote(notes: Notes) {
        viewModelScope.launch {
            dao.insertNote(notes)
        }
    }

    fun fetchUser(email: String, password: String): LiveData<Users> {
        return dao.fetchUserEmail(email, password)
    }

    fun fetchUserPhone(phone: String, password: String): LiveData<Users> {
        return dao.fetchUserPhone(phone, password)
    }

    fun fetchUserNote(email: String): LiveData<List<Notes>> {
        return dao.fetchUserNotes(email)
    }

    fun getAllUsers(): LiveData<List<Users>> {
        return dao.fetchUsers()
    }

    fun getAllNotes(): LiveData<List<Notes>> {
        return dao.fetchAllNotes()
    }
}

class UserViewModelFactory(var dao: DAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown view model class")
    }
}
