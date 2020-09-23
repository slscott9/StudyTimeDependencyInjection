package com.example.studytimetwo.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.studytimetwo.data.StudySession

interface StudyRepository {


    //New method to get last seven sessions hours
    fun getLastSevenSessionsHours(currentMonth: Int, currentDayOfMonth: Int): LiveData<List<Float>>

    fun getSessionHoursForMonth(currentMonth: Int): LiveData<List<Float>>



    fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int): LiveData<List<StudySession>>

    fun getAllSessionsWithMatchingMonth(monthSelected: Int): LiveData<List<StudySession>> //returns live data so it does not need suspend. Room takes care of coroutines for us


    suspend fun insertStudySession(study: StudySession)

    suspend fun getCurrentStudySession(currentDate: String): StudySession




     fun getYearsWithSessions(currentYear: Int): LiveData<List<Int>>


     suspend fun getMonthWithSelectedYear(yearSelected : Int): List<Int>


}