package com.android.chatnappsummers

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.chatnappsummers.databinding.ActivityMainBinding
import com.android.chatnappsummers.utils.DialogUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        // showing dialog
        binding.ivAddContactOrGroup.setOnClickListener {
            DialogUtils.showAttachmentsDialog(this, it, R.layout.add_friend_group_dialog)
        }

        binding.ivSearchChat.setOnClickListener {
            startActivity(Intent(this, ChatConversationActivity::class.java))
        }
    }
}