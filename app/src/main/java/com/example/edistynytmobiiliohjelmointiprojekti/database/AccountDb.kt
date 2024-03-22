package com.example.edistynytmobiiliohjelmointiprojekti.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert


@Entity(tableName = "access_tokens")
data class AccessToken(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accessToken: String
)

@Dao
abstract class AccessTokenDao {
    @Upsert
    abstract suspend fun insertToken(token: AccessToken)

    @Query("SELECT * FROM access_tokens LIMIT 1")
    abstract suspend fun getAccessToken(): AccessToken?

    @Query("DELETE FROM access_tokens")
    abstract suspend fun clearAccessToken()
}

@Database(
    entities = [AccessToken::class],
    version = 1,
    exportSchema = false
)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accessTokenDao(): AccessTokenDao
}
