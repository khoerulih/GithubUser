package com.example.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.remote.responses.DetailUserResponse
import com.example.githubuser.data.remote.responses.ListUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _listFollow = MutableLiveData<List<ListUser>>()
    val listFollow: LiveData<List<ListUser>> = _listFollow

    private val _favoriteUser = MutableLiveData<UserEntity>()
    private val favoriteUser: LiveData<UserEntity> = _favoriteUser

    private val _isFollowDataNullOrBlank = MutableLiveData<Boolean>()
    val isFollowDataNullOrBlank: LiveData<Boolean> = _isFollowDataNullOrBlank

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val userRepository: UserRepository = UserRepository(application)

    fun showDetailUser(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUsers(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isError.value = false
                    _detailUser.value = response.body()
                    _favoriteUser.value = response.body()?.let { user ->
                        UserEntity(
                            login = user.login,
                            avatarUrl =  user.avatarUrl
                        )
                    }
                } else {
                    _isError.value = true
                    Log.e(DetailActivity.TAG, "onFailureResponse : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(DetailActivity.TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun showListFollower(username: String?) {
        val client = ApiConfig.getApiService().getUsersFollower(username)
        client.enqueue(object : Callback<List<ListUser>> {
            override fun onResponse(
                call: Call<List<ListUser>>,
                response: Response<List<ListUser>>
            ) {
                if (response.isSuccessful) {
                    if(response.body().isNullOrEmpty()){
                        _isFollowDataNullOrBlank.value = true
                    }
                    _listFollow.value = response.body()
                } else {
                    Log.e(DetailActivity.TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListUser>>, t: Throwable) {
                Log.e(DetailActivity.TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun showListFollowing(username: String?) {
        val client = ApiConfig.getApiService().getUsersFollowing(username)
        client.enqueue(object : Callback<List<ListUser>> {
            override fun onResponse(
                call: Call<List<ListUser>>,
                response: Response<List<ListUser>>
            ) {
                if (response.isSuccessful) {
                    if(response.body().isNullOrEmpty()){
                        _isFollowDataNullOrBlank.value = true
                    }
                    _listFollow.value = response.body()
                } else {
                    Log.e(DetailActivity.TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListUser>>, t: Throwable) {
                Log.e(DetailActivity.TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun setFavorite() = favoriteUser.value?.let { userRepository.getUserFavorite(it) }
}