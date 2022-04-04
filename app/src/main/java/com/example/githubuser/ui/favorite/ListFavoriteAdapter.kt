package com.example.githubuser.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.ItemRowUserBinding
import com.example.githubuser.ui.detail.DetailActivity

class ListFavoriteAdapter(private val listFavorite: List<UserEntity>) :
    RecyclerView.Adapter<ListFavoriteAdapter.ListFavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFavoriteViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListFavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListFavoriteViewHolder, position: Int) {
        val (login, avatarUrl) = listFavorite[position]
        Glide.with(holder.itemView.context)
            .load(avatarUrl)
            .circleCrop()
            .into(holder.binding.imgItemAvatar)
        holder.binding.tvItemUsername.text = login
        holder.itemView.setOnClickListener {
            val goToDetailActivity = Intent(holder.itemView.context, DetailActivity::class.java)
            goToDetailActivity.putExtra(DetailActivity.EXTRA_USERNAME, login)
            holder.itemView.context.startActivity(goToDetailActivity)
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    class ListFavoriteViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)
}