package com.waslim.storyapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ActivitySettingBinding
import com.waslim.storyapp.viewmodel.DarkModeSettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val darkModeSettingViewModel by viewModels<DarkModeSettingViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.title = getString(R.string.pengaturan)

        setThemeSetting()
        getThemeSetting()
        actionLocationSetting()
    }

    private fun setThemeSetting() {
        binding.apply {
            switchModeDark.setOnCheckedChangeListener { _, isChecked ->
                switchModeDark.isChecked = isChecked
                darkModeSettingViewModel.setThemeSetting(isChecked)
            }
        }
    }

    private fun getThemeSetting() {
        binding.apply {
            darkModeSettingViewModel.getThemeSetting().observe(this@SettingActivity) {
                when {
                    it -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        switchModeDark.isChecked = true
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        switchModeDark.isChecked = false
                    }
                }
            }
        }
    }

    private fun actionLocationSetting() = binding.buttonGanti.setOnClickListener {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

}