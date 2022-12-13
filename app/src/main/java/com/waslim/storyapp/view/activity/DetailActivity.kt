package com.waslim.storyapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ActivityDetailBinding
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.viewmodel.DetailStoryViewModel
import com.waslim.storyapp.viewmodel.UserTokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailStoryViewModel>()
    private val userTokenViewModel by viewModels<UserTokenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.detail)

        val storyData = intent.getParcelableExtra<Story>(DATA_STORY) as Story

        when {
            storyData.id != null -> checkToken(storyData)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)
        menu.findItem(R.id.logout).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about -> {
            aboutMe()
            true
        }

        R.id.setting -> {
            setting()
            true
        }

        else -> true
    }

    private fun checkToken(story: Story) {
        userTokenViewModel.getToken().observe(this) {
            if (it != "") {
                getDetail(story.name, story.description, story.photoUrl)
                detailViewModel.getDetailStory(it, story.id.toString())
            }
        }
    }

    private fun getDetail(name: String?, description: String?, photoUrl: String?) {
        detailViewModel.detailStories.observe(this) {
            if (it != null) {
                binding.tvNameDetail.text = name
                binding.tvDescriptionDetail.text = description
                Glide
                    .with(applicationContext)
                    .load(photoUrl)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.ivDetail)
            }
        }
    }

    private fun aboutMe() = startActivity(Intent(this, AboutMeActivity::class.java))

    private fun setting() = startActivity(Intent(this, SettingActivity::class.java))

    companion object {
        const val DATA_STORY = "data_story"
    }
}