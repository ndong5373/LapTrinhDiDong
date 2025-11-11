package com.example.datastore01.ui

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences


import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


// DataStore duy nhất cho app
 val Context.appDataStore by preferencesDataStore(name="settings")

// Khóa lưu Dark mode
object settingsKeys{
    val Theme_Dark = booleanPreferencesKey("theme_dark")

}

//// 3) Hàm đọc (Flow<Boolean>) + ghi (suspend)
fun darkFlow(context: Context): Flow<Boolean> =
        context.appDataStore.data
            .catch { e -> if( e is IOException) emit(emptyPreferences()) else throw e }
            .map{ pref->pref[settingsKeys.Theme_Dark]?:false }
// ghi
suspend fun setDark(context: Context, enabled: Boolean) {
    context.appDataStore.edit { prefs ->
        prefs[settingsKeys.Theme_Dark] = enabled
    }
}
