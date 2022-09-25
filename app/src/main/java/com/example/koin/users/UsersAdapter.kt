package com.example.koin.users

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.koin.databinding.UserItemsBinding
import com.example.domain.model.User

class UsersAdapter(
    private val list: ArrayList<User>,
    private val onUserClick: (view: View) -> Unit,
) : RecyclerView.Adapter<UsersAdapter.UsersHolder>() {

    inner class UsersHolder(private val binding: UserItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {
            binding.apply {
                Log.d("zzz", "name ${item.name} playedBy ${item.occupation}")
                userName.text = item.name
                userDetail.text = item.occupation
                Glide.with(itemView.context).load(item.image).into(userPic)
                root.setOnClickListener {
                    onUserClick(binding.root)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserItemsBinding.inflate(inflater, parent, false)
        return UsersHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun update(newList: List<User>) {
        list.clear()
        list.addAll(newList)
        notifyItemRangeChanged(0, list.size)
    }
}