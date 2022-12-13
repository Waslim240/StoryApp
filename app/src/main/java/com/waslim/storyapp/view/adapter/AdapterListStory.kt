package com.waslim.storyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ItemStoryBinding
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.view.activity.DetailActivity

class AdapterListStory : RecyclerView.Adapter<AdapterListStory.ViewHolder>() {

    private var listStories: List<Story>? = null

    fun setStoriesData(stories: List<Story>) {
        this.listStories = stories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stories = listStories?.get(position)
        holder.apply {
            itemView.apply {
                binding.apply {
                    Glide.with(context)
                        .load(stories?.photoUrl)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(ivStories)
                    tvNameStories.text = stories?.name
                    tvDescriptionStories.text = stories?.description

                    setOnClickListener {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.DATA_STORY, stories)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as Activity,
                                Pair(ivStories, "iv_detail"),
                                Pair(tvNameStories, "tv_name_detail"),
                                Pair(tvDescriptionStories, "tv_description_detail"),
                            )
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return when (listStories) {
            null -> 0
            else -> listStories!!.size
        }
    }

    class ViewHolder (var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

}
