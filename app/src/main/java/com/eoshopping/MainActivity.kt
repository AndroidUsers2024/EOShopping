package com.eoshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

val db:FirebaseFirestore by lazy { Firebase.firestore }
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val et_user = findViewById<EditText>(R.id.et_user)
        val et_password = findViewById<EditText>(R.id.et_password)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val btn_register = findViewById<Button>(R.id.btn_register)

        val progressBar = findViewById<ProgressBar>(R.id.login_progress_bar)

        btn_login.setOnClickListener(View.OnClickListener {
            val email = et_user.text.toString()
            val password = et_password.text.toString()
            if(email.isEmpty()){
                et_user.setError("Cannot be empty")
                return@OnClickListener
            }
            if(password.isEmpty()){
                et_password.setError("Cannot be empty")
                return@OnClickListener
            }

            progressBar.visibility = View.VISIBLE
            fAuth.signInWithEmailAndPassword(et_user.text.toString(),et_password.text.toString()).addOnCompleteListener(
                OnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                        progressBar.visibility = View.GONE
                    }else{
                        Toast.makeText(this,"failed : ${it.exception?.message}",Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.GONE
                    }
                })
        })

        btn_register.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        })

        /*setContentView(R.layout.activity_main)
        var bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        var navController = Navigation.findNavController(this,R.id.host_fragment )
        NavigationUI.setupWithNavController(bottomNavigation,navController)*/
    }
}