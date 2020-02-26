package com.message.toschat.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.message.toschat.toschat.R
import com.message.toschat.models.User
import com.message.toschat.services.SingleTon
import com.message.toschat.utils.Constance
import kotlinx.android.synthetic.main.user_item_view.view.*

class UserAdapter(var userList: MutableList<User>, var context: Context, var listener: OnItemClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holderUser: UserViewHolder, position: Int) {
        val user = SingleTon.getCurrentUser(context, Constance.SINGLE_USER)
        if (user?.userId != userList[position].userId) {
            holderUser.Bind(userList[position], listener)
        }
//        else {
//            userList.remove(userList[position])
//        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName = itemView.txt_name
        private val userImage = itemView.img_profile

        fun Bind(user: User, listener: OnItemClickListener) {
            userName.text = user.userName
            Glide.with(itemView.context)
                    .load(user.userProfile)
                    .into(userImage)
            itemView.setOnClickListener {
                listener.onItemClick(user)
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }
}
