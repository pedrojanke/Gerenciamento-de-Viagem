package com.example.gerenciamentodeviagem.data.repository

import com.example.gerenciamentodeviagem.data.local.UserDao
import com.example.gerenciamentodeviagem.data.models.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun getUser(username: String, password: String) = userDao.getUser(username, password)
}
