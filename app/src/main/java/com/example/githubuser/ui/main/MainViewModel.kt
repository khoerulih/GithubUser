package com.example.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.remote.responses.ListUser
import com.example.githubuser.data.remote.responses.SearchUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ListUser>>()
    val listUser: LiveData<List<ListUser>> = _listUser
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataError = MutableLiveData<Boolean>()
    val isDataError: LiveData<Boolean> = _isDataError

    init {
        showListUser()
    }

    fun showListUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<ListUser>> {
            override fun onResponse(call: Call<List<ListUser>>, response: Response<List<ListUser>>) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _isDataError.value = false
                    _listUser.value = response.body()
                } else {
                    _isDataError.value = true
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ListUser>>, t: Throwable) {
                _isLoading.value = false
                _isDataError.value = true
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun searchUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(username)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(call: Call<SearchUserResponse>, response: Response<SearchUserResponse>) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    _listUser.value = response.body()?.items
                    _isDataError.value = _listUser.value.isNullOrEmpty()
                } else {
                    _isDataError.value = true
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataError.value = true
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }


    companion object {
        private const val TAG = "MainViewModel"
    }
}