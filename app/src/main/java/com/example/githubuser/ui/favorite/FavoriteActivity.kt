package com.example.githubuser.ui.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.ViewModelFactory


class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory = ViewModelFactory.getInstance(this@FavoriteActivity.application)
        val favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        binding?.rvFavorite?.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvFavorite?.addItemDecoration(itemDecoration)

        favoriteViewModel.getUserFavorite().observe(this){ listFavorite ->
            binding?.rvFavorite?.adapter = ListFavoriteAdapter(listFavorite)
        }

        supportActionBar?.title = "List Favorite"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}