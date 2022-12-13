package com.waslim.storyapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.waslim.storyapp.R
import com.waslim.storyapp.viewmodel.DarkModeSettingViewModel
import com.waslim.storyapp.viewmodel.UserTokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val userTokenViewModel by viewModels<UserTokenViewModel>()
    private val darkModeSettingViewModel by viewModels<DarkModeSettingViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashHandler()
        checkDarkMode()
    }

    private fun splashHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkToken()
        }, DELAY)
    }

    private fun checkDarkMode() {
        darkModeSettingViewModel.getThemeSetting().observe(this) {
            when {
                it -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun checkToken(){
        userTokenViewModel.getToken().observe(this) {
            when {
                it != "" -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                }
                else -> {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    companion object {
        const val DELAY = 3000L
    }
}