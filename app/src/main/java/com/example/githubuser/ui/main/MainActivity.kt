package com.example.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.ui.ListUserAdapter
import com.example.githubuser.data.remote.responses.ListUser
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.utils.SettingPreferences
import com.example.githubuser.utils.SettingViewModel
import com.example.githubuser.utils.SettingViewModelFactory
import com.example.githubuser.ui.favorite.FavoriteActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.rvUser?.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding?.rvUser?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvUser?.addItemDecoration(itemDecoration)

        mainViewModel.listUser.observe(this) { listUsers ->
            setListData(listUsers)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isDataError.observe(this) {
            showErrorMessage(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val toggleTheme = menu.findItem(R.id.mode)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSetting().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                toggleTheme.isChecked = true
                toggleTheme.setIcon(R.drawable.ic_light_mode_24)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                toggleTheme.isChecked = false
                toggleTheme.setIcon(R.drawable.ic_dark_mode_24)
            }
        }

        toggleTheme.setOnMenuItemClickListener {
            if (!it.isChecked) {
                it.isChecked = true
                settingViewModel.saveThemeSetting(it.isChecked)
            } else {
                it.isChecked = false
                settingViewModel.saveThemeSetting(it.isChecked)
            }
            true
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    mainViewModel.showListUser()
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                true
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.mode -> {
                true
            }
            else -> throw IllegalArgumentException("Unknown menu item: " + item.itemId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setListData(listUsers: List<ListUser>) {
        val users = ArrayList<ListUser>()
        for (user in listUsers) {
            val list = ListUser(
                user.login,
                user.avatarUrl
            )
            users.add(list)
        }
        val adapter = ListUserAdapter(users)
        binding?.rvUser?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun showErrorMessage(isError: Boolean) {
        if (isError) {
            Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show()
        }
    }
}