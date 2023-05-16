package com.stdio.mangoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.stdio.mangoapp.domain.models.ProfileData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ProfileData::class], version = 1, exportSchema = false)
abstract class ProfileDataDB : RoomDatabase() {

    abstract fun profileDataDao(): ProfileDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val profileDao = database.profileDataDao()
                    profileDao.deleteAll()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileDataDB? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ProfileDataDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfileDataDB::class.java,
                    "profile_database"
                ).addCallback(WordDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}