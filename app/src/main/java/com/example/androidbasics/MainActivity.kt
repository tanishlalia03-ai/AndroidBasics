package com.example.androidbasics

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidbasics.RelativeLayout.RelativeLayoutActivity

class MainActivity : AppCompatActivity() {

    lateinit var log : Button
    lateinit var e1 : EditText
    lateinit var pass : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        log=findViewById<Button>(R.id.login)
        e1=findViewById<EditText>(R.id.email1)
        pass=findViewById<EditText>(R.id.password1)

        log.setOnClickListener {
            var e2 = e1.text.toString()
            var pass2= pass.text.toString()

            if(e2.toString() == "tanishlalia@gmail.com" && pass2.toString()=="123"){

                val intent = Intent(this, RelativeLayoutActivity:: class.java)
                startActivity(intent)
            }

            else {
                val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
                log.startAnimation(shake)
                val toast = Toast.makeText(this, "Please enter correct", Toast.LENGTH_SHORT).show()
            }
        }



    }
}