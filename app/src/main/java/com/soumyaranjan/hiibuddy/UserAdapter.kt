package com.soumyaranjan.hiibuddy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class UserAdapter(val context : Context, var userList : ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val container = itemView.findViewById<RelativeLayout>(R.id.user_list_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_list_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var currentUser = userList[position]
        holder.txtName.text = currentUser.name

        holder.container.setOnClickListener {
            val toChat = Intent(context, Chat::class.java)

            toChat.putExtra("name", currentUser.name)
            toChat.putExtra("uid", currentUser.uid)

            context.startActivity(toChat)
        }
    }
}