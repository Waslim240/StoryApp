package com.waslim.storyapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ActivityRegisterBinding
import com.waslim.storyapp.model.showToast
import com.waslim.storyapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        register()
        goToLogin()
        checkError()
        checkLength()

    }

    private fun register() = binding.btnRegister.setOnClickListener {
        val name = binding.etNameRegister.text.toString()
        val email = binding.etEmailRegister.text.toString()
        val password = binding.etPasswordRegister.text.toString()

        showLoading(true)
        registerViewModel.register.observe(this) { RegisterResponse ->
            if (RegisterResponse.error != true) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                finish()
                showToast(getString(R.string.berhasil_membuat_akun))
                showLoading(false)
            }
        }
        registerViewModel.registerUser(name, email, password)
        closedKeyboard()
    }

    private fun checkError() {
        showLoading(true)
        registerViewModel.error.observe(this) {
            if (it) {
                showToast(getString(R.string.gagal_membuat_akun))
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = registerViewModel.isLoading.observe(this) {
        binding.progressBarRegister.visibility = when {
            isLoading -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun goToLogin() = binding.loginHere.setOnClickListener {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    private fun closedKeyboard() {
        val view: View? = currentFocus
        val inputMethodManager: InputMethodManager
        when {
            view != null -> {
                inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun checkLength() = binding.apply{
        etPasswordRegister.doAfterTextChanged { text ->
            when {
                text!!.length in 1..5 -> textInputLayoutPasswordRegister.isEndIconVisible = false
                text.length >= 5 || text.isEmpty() -> textInputLayoutPasswordRegister.isEndIconVisible = true
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}