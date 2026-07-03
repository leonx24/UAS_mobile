package com.exampl.worksyncc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exampl.worksyncc.databinding.ActivityLoginBinding
import com.exampl.worksyncc.ui.dashboard.DashboardActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Please fill all fields", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proper Login Logic
            val user = when {
                email == "pm@worksync.com" && password == "pm123" -> {
                    "Leon" to "Project Manager"
                }
                email == "staff@worksync.com" && password == "staff123" -> {
                    "Andi" to "Karyawan"
                }
                else -> null
            }

            if (user != null) {
                val tokenManager = com.exampl.worksyncc.utils.TokenManager(this)
                tokenManager.saveUser(user.first, user.second, "token_${user.second.lowercase()}")

                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                android.widget.Toast.makeText(this, "Invalid email or password", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}
