package de.gnarly.got.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
	@Provides
	fun provideGoTDatabase(@ApplicationContext context: Context): GoTDatabase =
		Room.databaseBuilder(context, GoTDatabase::class.java, "got.db")
			.build()

	@Provides
	fun provideHouseDao(db: GoTDatabase): HouseDao =
		db.getHouseDao()
}