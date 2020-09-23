package com.example.studytimetwo.data.repository

import androidx.lifecycle.LiveData
import com.example.studytimetwo.data.StudyDao
import com.example.studytimetwo.data.StudySession
import javax.inject.Inject

class StudyRepoImpl @Inject constructor(
    private val studyDao: StudyDao
) : StudyRepository{



    override fun getLastSevenSessionsHours(
        currentMonth: Int,
        currentDayOfMonth: Int
    ): LiveData<List<Float>> {
        return studyDao.getLastSevenSessionsHours(currentMonth, currentDayOfMonth)
    }

    override fun getSessionHoursForMonth(currentMonth: Int): LiveData<List<Float>> {
        return studyDao.getSessionHoursForMonth(currentMonth)
    }

    override fun getLastSevenSessions(
        currentMonth: Int,
        currentDayOfMonth: Int
    ): LiveData<List<StudySession>> {
        return studyDao.getLastSevenSessions(currentMonth, currentDayOfMonth )
    }

    override fun getAllSessionsWithMatchingMonth(monthSelected: Int): LiveData<List<StudySession>> {
        return studyDao.getAllSessionsWithMatchingMonth(monthSelected)
    }

    override suspend fun insertStudySession(study: StudySession) {
        studyDao.upsertStudySession(study)
    }

    override suspend fun getCurrentStudySession(currentDate: String): StudySession {
        return studyDao.getCurrentStudySession(currentDate)
    }

    override  fun getYearsWithSessions(currentYear: Int): LiveData<List<Int>> {
        return studyDao.getYearsWithSessions(currentYear)
    }

    override suspend fun getMonthWithSelectedYear(yearSelected: Int): List<Int> {
        return studyDao.getMonthsWithSelectedYear(yearSelected)
    }


}