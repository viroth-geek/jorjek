package com.message.toschat.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.message.toschat.toschat.R
import com.message.toschat.model.User
import com.message.toschat.toschat.BR
import com.message.toschat.toschat.databinding.UserItemViewBinding


class UserAdapter(var userList: ArrayList<User>, var context: Context, var listener: OnItemClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userItemViewBinding: UserItemViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.user_item_view, parent, false)
        return UserViewHolder(userItemViewBinding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holderUser: UserViewHolder, position: Int) {
        holderUser.bind(userList[position], listener)
    }

    inner class UserViewHolder(private val binding: UserItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, listener: OnItemClickListener) {
            binding.setVariable(BR.user, user)
            binding.executePendingBindings()
            itemView.setOnClickListener {
                listener.onItemClick(user)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }
}
