package de.gnarly.got.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HousesPagingKeyStore @Inject constructor(
	@ApplicationContext private val context: Context
) {

	private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "houses_paging.store")

	val nextPage: Flow<Int?> = context.dataStore.data.map {
		val value = it[NEXT_PAGE]
		if (value == -1) {
			null
		} else {
			value
		}
	}

	suspend fun saveNextPage(nextPage: Int?) {
		context.dataStore.edit { settings ->
			val value = nextPage ?: -1

			settings[NEXT_PAGE] = value
		}
	}

	suspend fun resetNextPage() {
		saveNextPage(1)
	}

	companion object {
		private val NEXT_PAGE = intPreferencesKey("next_page")
	}
}