package com.example.githubuser.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.ui.detail.DetailActivity
import com.example.githubuser.data.remote.responses.ListUser
import com.example.githubuser.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUser: List<ListUser>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (login, avatarUrl) = listUser[position]
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

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)
}