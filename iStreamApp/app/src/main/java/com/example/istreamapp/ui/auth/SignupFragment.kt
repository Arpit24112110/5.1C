package com.example.istreamapp.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.istreamapp.R
import com.example.istreamapp.data.AppDatabase
import com.example.istreamapp.model.User
import kotlinx.coroutines.launch

class SignupFragment : Fragment(R.layout.fragment_signup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etFullName = view.findViewById<EditText>(R.id.etFullName)
        val etUsername = view.findViewById<EditText>(R.id.etSignupUsername)
        val etPassword = view.findViewById<EditText>(R.id.etSignupPassword)
        val etConfirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword)
        val btnCreateAccount = view.findViewById<Button>(R.id.btnCreateAccount)

        val db = AppDatabase.getDatabase(requireContext())
        val dao = db.appDao()

        btnCreateAccount.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val existingUser = dao.getUserByUsername(username)

                if (existingUser != null) {
                    Toast.makeText(requireContext(), "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    dao.insertUser(
                        User(
                            fullName = fullName,
                            username = username,
                            password = password
                        )
                    )

                    Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()

                    parentFragmentManager.popBackStack()
                }
            }
        }
    }
}