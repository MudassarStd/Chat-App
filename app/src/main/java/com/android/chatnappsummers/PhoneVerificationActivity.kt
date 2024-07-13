package com.android.chatnappsummers

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.chatnappsummers.databinding.ActivityPhoneVerificationBinding

class PhoneVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPhoneVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // passing user phone for OTP verification
        binding.btnSendPhoneNumber.setOnClickListener {
            val phoneNumber = binding.etUserPhoneNumber.text.toString()
            val intent = Intent(this, OTPVerificationActivity::class.java)
            intent.putExtra("phoneNumber", phoneNumber)
            startActivity(intent)

        }
    }

}
