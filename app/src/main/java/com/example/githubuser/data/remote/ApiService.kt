package com.example.githubuser.data.remote

import com.example.githubuser.BuildConfig
import com.example.githubuser.data.remote.responses.DetailUserResponse
import com.example.githubuser.data.remote.responses.ListUser
import com.example.githubuser.data.remote.responses.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users")
    fun getUsers() : Call<List<ListUser>>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("search/users")
    fun searchUsers(
        @Query("q") login: String
    ) : Call<SearchUserResponse>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{login}")
    fun getDetailUsers(
        @Path("login") login: String?
    ) : Call<DetailUserResponse>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{login}/following")
    fun getUsersFollowing(
        @Path("login") login: String?
    ) : Call<List<ListUser>>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{login}/followers")
    fun getUsersFollower(
        @Path("login") login: String?
    ) : Call<List<ListUser>>
}