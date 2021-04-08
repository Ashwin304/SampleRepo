package com.example.sampleloginapp.io.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sampleloginapp.io.model.Source


@Entity(tableName = "News_Table")
data class NewsData(
      @PrimaryKey(autoGenerate = false)
      val url: String
)