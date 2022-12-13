package com.waslim.storyapp.model.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserTokenPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun setToken(token: String) {
        context.userToken.edit {
            it[DATASTORE_KEY] = token
        }
    }

    suspend fun removeToken() {
        context.userToken.edit {
            it[DATASTORE_KEY] = ""
        }
    }

    fun getToken() : Flow<String> {
        return context.userToken.data.map {
            it[DATASTORE_KEY] ?: ""
        }
    }

    companion object {
        private const val DATASTORE_TOKEN = "token_preferences"

        private val DATASTORE_KEY = stringPreferencesKey ("datastore_key")

        private val Context.userToken by preferencesDataStore(name = DATASTORE_TOKEN)
    }
}