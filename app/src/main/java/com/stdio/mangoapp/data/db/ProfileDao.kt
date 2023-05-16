package com.stdio.mangoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.stdio.mangoapp.domain.models.ProfileData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM ProfileData LIMIT 1")
    fun getUserData(): Flow<ProfileData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profileData: ProfileData)

    @Update
    suspend fun update(profileData: ProfileData)

    @Query("DELETE FROM ProfileData")
    suspend fun deleteAll()
}