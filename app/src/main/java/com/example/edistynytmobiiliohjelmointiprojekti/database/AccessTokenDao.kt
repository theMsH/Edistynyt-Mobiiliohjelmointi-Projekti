package com.example.edistynytmobiiliohjelmointiprojekti.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AccessTokenDao {
    @Upsert
    suspend fun insert(token: AccessToken)

    @Query("SELECT * FROM access_tokens LIMIT 1")
    suspend fun getAccessToken(): AccessToken?

    @Query("DELETE FROM access_tokens")
    suspend fun clearAccessToken()
}