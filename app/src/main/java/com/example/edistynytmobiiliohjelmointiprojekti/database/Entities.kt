package com.example.edistynytmobiiliohjelmointiprojekti.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "access_tokens")
data class AccessToken(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accessToken: String
)
