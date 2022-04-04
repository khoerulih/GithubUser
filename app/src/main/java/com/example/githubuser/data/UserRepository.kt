package com.example.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.data.local.room.UserDao
import com.example.githubuser.data.local.room.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {

    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getListUserFavorite(): LiveData<List<UserEntity>> = mUserDao.getAllUserFavorite()

    fun getUserFavorite(user: UserEntity) {
        executorService.execute{
            if (mUserDao.isFavorite(user.login)){
                mUserDao.deleteUser(user)
            } else {
                mUserDao.insertUser(user)
            }
        }
    }
}