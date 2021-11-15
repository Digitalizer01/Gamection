package com.example.gamection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.se.omapi.Session
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val Text = findViewById<TextView>(R.id.textView_Register_Link)
        Text.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            var analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
            var bundle = Bundle()
            bundle.putString("message", "Integración de Firebase completa")
            analytics.logEvent("InitScreen", bundle)
            startActivity(i)
        }

        val Button = findViewById<TextView>(R.id.button_Login)
        val emailText = findViewById<TextView>(R.id.editTextText_User_Name)
        val passText = findViewById<TextView>(R.id.editTextTextUser_Password)
        Button.setOnClickListener {
            if (emailText.text.isNotEmpty() && passText.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        emailText.text.toString(),
                        passText.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val toast = Toast.makeText(this, "Logeado", Toast.LENGTH_SHORT)
                            toast.setMargin(50f, 50f)
                            toast.show()
                            //val Button = findViewById<TextView>(R.id.button_Login)
                            //Button.setOnClickListener {
                            startActivity(Intent(this, SessionActivity::class.java))
                        } else {
                            val toast = Toast.makeText(this, "No logeado", Toast.LENGTH_SHORT)
                            toast.setMargin(50f, 50f)
                            toast.show()
                        }
                    }
            }
        }
    }
}