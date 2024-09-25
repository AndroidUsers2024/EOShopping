package com.eoshopping

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegistrationActivity : AppCompatActivity() {
    val fAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var iv_profile: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)
        val et_user = findViewById<EditText>(R.id.et_user)
        val et_email = findViewById<EditText>(R.id.et_email)
        val et_password = findViewById<EditText>(R.id.et_password)
        val et_cnfrm_password = findViewById<EditText>(R.id.et_cnfrm_password)
        val et_age = findViewById<EditText>(R.id.et_age)
        val et_mobile = findViewById<EditText>(R.id.et_mobile)

        val btn_save = findViewById<Button>(R.id.btn_save)
        val btn_uploadImage = findViewById<Button>(R.id.btn_upload_image)
        iv_profile = findViewById(R.id.iv_profile)
        btn_uploadImage.setOnClickListener(View.OnClickListener {
            val openGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalery,111)
        })

        progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        btn_save.setOnClickListener(View.OnClickListener {

            val email = et_email.text.toString()
            val password = et_password.text.toString()
            val cnfrm_password = et_cnfrm_password.text.toString()
            val userName = et_user.text.toString()
            val age = et_age.text.toString()
            val mobile = et_mobile.text.toString()

            if(email.isEmpty()){
                et_email.setError("Cannot be empty")
                return@OnClickListener
            }
            if(password.isEmpty()){
                et_password.setError("Cannot be empty")
                return@OnClickListener
            }
            if(cnfrm_password.isEmpty() || !cnfrm_password.equals(password)){
                et_cnfrm_password.setError("should match with password")
                return@OnClickListener
            }
            if(userName.isEmpty()){
                et_user.setError("Cannot be empty")
                return@OnClickListener
            }
            if(age.isEmpty()){
                et_age.setError("Cannot be empty")
                return@OnClickListener
            }
            if(mobile.isEmpty()){
                et_mobile.setError("Cannot be empty")
                return@OnClickListener
            }

            progressBar.visibility = View.VISIBLE

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                OnCompleteListener {
                    if(it.isSuccessful){
                        val map = mutableMapOf<String,String>()
                        map.put("name",userName)
                        map.put("password",password)
                        map.put("age",age)
                        map.put("mobile",mobile)
                        map.put("email",email)
                        map.put("imageUri", "users/${fAuth.currentUser!!.uid}/profile.jpg")
                        val userId = fAuth.currentUser!!.uid
                        db.collection("Users").document(userId).set(map)
                            .addOnSuccessListener {
                                Toast.makeText(this,"Registration Success",Toast.LENGTH_LONG).show()
                                uploadImageToFirebase(imageUri)

                            }
                            .addOnFailureListener {
                                Toast.makeText(this,it.message.toString(),Toast.LENGTH_LONG).show()
                            }
                    }else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.GONE
                    }
                })



        })

    }

    private fun uploadImageToFirebase(imageUri : Uri) {
        val fileRef = FirebaseStorage.getInstance().getReference().child("users/${fAuth.currentUser!!.uid}/profile.jpg")
        fileRef.putFile(imageUri).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Image Success", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
            }
            startActivity(Intent(this,MainActivity::class.java))
            progressBar.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111){
            if(resultCode==Activity.RESULT_OK){
                imageUri = data?.data!!
                iv_profile.setImageURI(imageUri)
            }
        }
    }

}


