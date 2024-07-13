package com.android.chatnappsummers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.chatnappsummers.databinding.ActivityOtpverificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpverificationBinding
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var verificationIDOTPSent = ""
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    private var otp : String? = "000123"
    private val TAG  = "jfsdklfjslsdfjfksd"
    private lateinit var phoneNumber :String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.etOTP.requestFocus()


        phoneNumber = "+92"+intent.getStringExtra("phoneNumber")
        binding.tvUserPhoneNumber.text = "Phone: "+phoneNumber

        Log.d(TAG,"PhoneNumber: $phoneNumber")


        // passing number
        sendOTP(phoneNumber,false)



    }

    private fun sendOTP(phone : String, resend : Boolean)
    {
        Log.d(TAG,"inside sendOTP: ")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
            {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                    // if automatic verification happens
                    otp = credential.smsCode.toString()
                    binding.etOTP.setText(otp)
                    signUp(credential)

                    Log.e(TAG,"OnVerificationSuccess: $credential")
                    Toast.makeText(this@OTPVerificationActivity, "OTP Sent Verification", Toast.LENGTH_SHORT).show()

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.e(TAG,"OnVerificationFailed: $p0")
                    Toast.makeText(this@OTPVerificationActivity, "OTP not sent", Toast.LENGTH_SHORT).show()

                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)

//                    verificationIDOTPSent = verificationId
                    resendToken = token

                    // verify OTP
                    verify(verificationId)

//
//                   startTimer()
                    Toast.makeText(this@OTPVerificationActivity, "OTP sent ", Toast.LENGTH_SHORT).show()
                    Log.d(TAG,"onCodeSent: string-> $verificationId , ResendToken $resendToken")
                }


            })
            .build()

//        if (resend)
//        {
//            Toast.makeText(this@OTPVerificationActivity, "OTP Resent ", Toast.LENGTH_SHORT).show()
//        }
//        else{
        PhoneAuthProvider.verifyPhoneNumber(options)
//        }
    }

    private fun verify(verificationId: String) {
        binding.btnVerifyOTP.setOnClickListener {

            val enteredOTP = binding.etOTP.text.toString()

            if(enteredOTP.isNotEmpty())
            {
                val credential = PhoneAuthProvider.getCredential(verificationId, enteredOTP)
                signUp(credential)
            }

        }
    }

    private fun signUp(otpCredentials: PhoneAuthCredential) {
        auth.signInWithCredential(otpCredentials)
            .addOnCompleteListener {
                if (it.isSuccessful)
                {
                    // storing in sharedPref
                    storeUserRegistrationStatus(true)
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, SignUpCredentialsActivity::class.java))
                }
                else{
                    storeUserRegistrationStatus(false)
                }
            }
    }



    private fun storeUserRegistrationStatus(isRegistered : Boolean)
    {
        val sharedPref : SharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isRegistered", isRegistered)
        editor.putString("phone", phoneNumber)
        editor.apply()
    }
}