package com.waslim.storyapp.repository

import com.waslim.storyapp.model.datastore.DarkModeSettingPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DarkModeSettingRepository @Inject constructor(private val darkModeSettingPreferences: DarkModeSettingPreferences) {
    suspend fun setThemeSetting(isDarkMode: Boolean) =
        darkModeSettingPreferences.setThemeSetting(isDarkMode)

    fun getThemeSetting() : Flow<Boolean> = darkModeSettingPreferences.getThemeSetting()
}