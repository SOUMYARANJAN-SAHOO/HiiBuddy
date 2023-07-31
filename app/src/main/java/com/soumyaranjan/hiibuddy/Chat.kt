package com.soumyaranjan.hiibuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.soumyaranjan.hiibuddy.databinding.ActivityChatBinding

class Chat : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding

    lateinit var chatRecyclerView: RecyclerView
    lateinit var chatAdapter: ChatAdapter

    lateinit var messageList: ArrayList<Message>

    var senderRoom : String? = null
    var recieverRoom : String? = null

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var recieverName = intent.getStringExtra("name")
        var recieverUId = intent.getStringExtra("uid")

        var senderUId = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = senderUId + recieverUId
        recieverRoom = recieverUId + senderUId

        database = FirebaseDatabase.getInstance().reference

        supportActionBar?.title = recieverName

        messageList = arrayListOf()

        chatRecyclerView = binding.chatRecycler
        chatRecyclerView.layoutManager = LinearLayoutManager(this@Chat)
        chatAdapter = ChatAdapter(this@Chat, messageList)
        chatRecyclerView.adapter = chatAdapter

        //Logic for adding the messages
        database.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })


        //Logic for sending the message
        binding.btnSend.setOnClickListener {
            val message = binding.edtMessageBox.text.toString()
            val messageObject = Message(message, senderUId!!)

            database.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject)
                .addOnSuccessListener {
                    database.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            binding.edtMessageBox.setText("")
        }
    }
}