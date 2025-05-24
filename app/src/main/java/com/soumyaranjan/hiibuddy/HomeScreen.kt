package com.soumyaranjan.hiibuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soumyaranjan.hiibuddy.databinding.ActivityMainBinding

class HomeScreen : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var recycler : RecyclerView
    lateinit var adapter: UserAdapter
    lateinit var userList : ArrayList<User>
    lateinit var mAuth : FirebaseAuth
    lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.getReference()

        userList = arrayListOf()
        recycler = binding.userListRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(this@HomeScreen, userList)
        recycler.adapter = adapter

        database.child("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val curentUser = postSnapshot.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != curentUser?.uid) {
                        userList.add(curentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeScreen, "Failed to load comments: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout){
            val toLogin = Intent(this@HomeScreen, Login::class.java)
            mAuth.signOut()
            finish()
            startActivity(toLogin)
        }



        return super.onOptionsItemSelected(item)
    }
}
