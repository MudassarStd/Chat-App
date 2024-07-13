package com.android.chatnappsummers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.chatnappsummers.databinding.ActivitySignUpCredentialsBinding
import com.android.chatnappsummers.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class SignUpCredentialsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpCredentialsBinding
    private lateinit var selectedUserProfileImage: Uri
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    companion object {
        private const val USER_PREFS = "user_prefs"
        private const val KEY_PHONE = "phone"
        private const val KEY_NAME = "name"
        private const val KEY_PHOTO = "photo"
        private const val KEY_USER_ID = "userId"
        private const val KEY_STORAGE_PHOTO_URL = "storagePhotoUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpCredentialsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val phone = sharedPreferences.getString(KEY_PHONE, "")

        binding.ivUserProfile.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnNext.setOnClickListener {
            val name = binding.etUserName.text.toString()

            if (name.isNotEmpty()) {
                editor.putString(KEY_NAME, name)
                editor.putString(KEY_PHOTO, selectedUserProfileImage.toString())
                editor.apply()

                val user = User(name, phone!!, selectedUserProfileImage.toString(), true)
                storeUserInCloud(user)
            } else {
                Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun storeUserInCloud(user: User) {
        firestore.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                val docId = documentReference.id
                editor.putString(KEY_USER_ID, docId)
                editor.apply()
                uploadProfileImage(selectedUserProfileImage, docId)
                Toast.makeText(this, "Profile Created Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadProfileImage(imageUri: Uri, userId: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("users_profile_pictures/$userId.jpg")

        imagesRef.putFile(imageUri)
            .addOnSuccessListener {
                // Get the download URL of the uploaded image
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    editor.putString(KEY_STORAGE_PHOTO_URL, imageUrl)
                    editor.apply()
                    Toast.makeText(this, "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show()


                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Error getting download URL: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.ivUserProfile.setImageURI(it)
            selectedUserProfileImage = it
        }
    }
}
