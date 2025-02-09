package com.example.practice

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.Models.userModel
import com.example.practice.Utils.USER_NODE
import com.example.practice.Utils.USER_PROFILE_FOLDER
import com.example.practice.Utils.uploadImage
import com.example.practice.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private lateinit var user: userModel

    // have to pass two arguments here contract and a callback
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
        uri?.let{
            uploadImage(uri , USER_PROFILE_FOLDER){
                if(it == null){
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }else{
                    user.image=it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val text = " <font color=#FF000000 > Don't have an account </font> <font color=#1E88E5> Login </font>  "
        binding.login.setText(Html.fromHtml(text))
        user = userModel()

        binding.Signupbutton.setOnClickListener {
            if (binding.name.editText?.text.toString().isEmpty() ||
                binding.email.editText?.text.toString().isEmpty() ||
                binding.password.editText?.text.toString().isEmpty()
            ) {
                Toast.makeText(this@SignupActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.editText?.text.toString(),
                    binding.password.editText?.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        user.name = binding.name.editText?.text.toString()
                        user.password = binding.password.editText?.text.toString()
                        user.email = binding.email.editText?.text.toString()

                        FirebaseFirestore.getInstance().collection(USER_NODE)
                            .document(FirebaseAuth.getInstance().currentUser!!.uid)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this@SignupActivity, "Signup Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,HomeActivity::class.java))
                                finish()
                            // You might want to navigate to another activity here
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this@SignupActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this@SignupActivity, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.addImage.setOnClickListener{
            launcher.launch("image/*")
        }

        binding.login.setOnClickListener{
            startActivity(Intent(this@SignupActivity  ,LoginActivity::class.java))
            finish()
        }

    }
}

