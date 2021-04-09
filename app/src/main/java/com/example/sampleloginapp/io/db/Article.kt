package com.example.sampleloginapp.io.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "News_Table")
@Parcelize
data class Article(
        @PrimaryKey(autoGenerate = false)
        val title: String,
        val description: String?,
        val url: String?,
        val urlToImage: String?,
        val publishedAt: String?,
        val favourite: Boolean?
): Parcelable
