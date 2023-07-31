package com.soumyaranjan.hiibuddy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(val context: Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val SENT_TEXT = 1
    val RECIEVED_TEXT = 2

    class SentViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
    }
    class RecievedViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val recievedMessage = itemView.findViewById<TextView>(R.id.recievedMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1 ){
            val view = LayoutInflater.from(context).inflate(R.layout.sent_message, parent,false)
            return SentViewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.recieve_message, parent,false)
            return RecievedViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            SENT_TEXT
        }else{
            RECIEVED_TEXT
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder::class == SentViewHolder::class){
            val viewHolder = holder as SentViewHolder

            viewHolder.sentMessage.text = currentMessage.message
        }else{
            val viewHolder = holder as RecievedViewHolder

            viewHolder.recievedMessage.text = currentMessage.message
        }


    }
}