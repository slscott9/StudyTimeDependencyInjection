package com.example.studytimetwo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_session_table")
data class StudySession(
    @PrimaryKey
    val date : String,
    val hours: Float,
    val minutes: Long,
    val weekDay: String,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int,
)