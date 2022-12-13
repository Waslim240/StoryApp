package com.waslim.storyapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.waslim.storyapp.R

class AboutMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        this.title = getString(R.string.tentang_saya)
    }
}