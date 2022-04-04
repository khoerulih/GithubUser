package com.example.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application): ViewModel() {

    private val mUserRepository: UserRepository = UserRepository(application)

    fun getUserFavorite(): LiveData<List<UserEntity>> = mUserRepository.getListUserFavorite()

}