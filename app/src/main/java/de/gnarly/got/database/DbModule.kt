package de.gnarly.got.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
	@Provides
	@Singleton
	fun provideGoTDatabase(@ApplicationContext context: Context): GoTDatabase =
		Room.databaseBuilder(context, GoTDatabase::class.java, "got.db")
			.build()

	@Provides
	@Singleton
	fun provideHouseDao(db: GoTDatabase): HouseDao =
		db.getHouseDao()

	@Provides
	@Singleton
	fun provideCharacterDao(db: GoTDatabase): CharacterDao =
		db.getCharacterDao()
}