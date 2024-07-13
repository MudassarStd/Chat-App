package com.android.chatnappsummers

import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.chatnappsummers.databinding.ActivityChatConversationBinding
import com.android.chatnappsummers.utils.DialogUtils

class ChatConversationActivity : AppCompatActivity() {
    private val binding: ActivityChatConversationBinding by lazy {
        ActivityChatConversationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.ivAddAttachment.setOnClickListener {
            DialogUtils.showAttachmentsDialog(this, it, R.layout.message_attachment_dialog)
        }

    }

}