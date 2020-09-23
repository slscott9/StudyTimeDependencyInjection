package com.example.studytimetwo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.studytimetwo.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class StudyDaoTest {

    private lateinit var database: StudyDatabase
    private lateinit var dao: StudyDao
    private lateinit var studySesion: StudySession

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule() //run each test synchronously

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StudyDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.studyDao()

        studySesion = StudySession(
            date = "08/27/1994",
            hours = 8F,
            minutes = 480,
            weekDay = "WEDNESDAY",
            dayOfMonth = 8,
            month = 8,
            year = 1994
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun get_last_seven_session_hours() = runBlockingTest{
        dao.insertStudySession(studySesion)


        val lastSevenStudySessions = dao.getLastSevenSessions(8, 8)
        assertThat(lastSevenStudySessions).isNotNull()

    }

    @Test
    fun getSesionsHoursForMonth() = runBlockingTest{

        dao.insertStudySession(studySesion)
        val hoursForMonth = dao.getSessionHoursForMonth(8).getOrAwaitValue()
        assertThat(hoursForMonth.size).isEqualTo(1)
    }

    @Test
    fun getCurrentStudySessionWithDate() = runBlockingTest {
        dao.insertStudySession(studySesion)
        val studySessionWithDate = dao.getCurrentStudySession("08/27/1994")

        assertThat(studySessionWithDate).isNotNull()
    }

    @Test
    fun getMonthsWithSelectedYear() = runBlockingTest {
        dao.insertStudySession(studySesion)

        val listOfYears = dao.getYearsWithSessions(1994)

//        assertThat(listOfYears).isNotEmpty()
    }

    @Test
    fun getMonthsWithSelectedYEar() = runBlockingTest {
        dao.insertStudySession(studySesion)

        val monthsList = dao.getMonthsWithSelectedYear(1994)

        assertThat(monthsList).isNotEmpty()
    }

    @Test
    fun updateStudySessionWithNew() = runBlockingTest {

        dao.insertStudySession(studySesion)

        val newStudySession = StudySession(
            date = "08/27/1994",
            hours = 16F,
            minutes = 480,
            weekDay = "THRURSDAY",
            dayOfMonth = 9,
            month = 9,
            year = 1995
        )

        dao.upsertStudySession(newStudySession)
        val updatedStudySession = dao.getCurrentStudySession("08/27/1994")

        assertThat(updatedStudySession).isNotNull()
    }








}