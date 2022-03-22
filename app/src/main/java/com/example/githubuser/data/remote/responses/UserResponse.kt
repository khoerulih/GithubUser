package com.example.githubuser.data.remote.responses

import com.google.gson.annotations.SerializedName


data class SearchUserResponse(
	@field:SerializedName("items")
	val items: List<ListUser>
)

data class ListUser(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,
)

data class DetailUserResponse(
	@field:SerializedName("following_url")
	val followingUrl: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("company")
	val company: String?,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("public_repos")
	val publicRepos: Int,

	@field:SerializedName("followers_url")
	val followersUrl: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("location")
	val location: String?,
)
