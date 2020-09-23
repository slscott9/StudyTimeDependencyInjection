package com.example.studytimetwo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudyDao {

    //Changes for transformations
    @Query("select hours from study_session_table where month = :currentMonth and dayOfMonth between :currentDayOfMonth - 6 and :currentDayOfMonth order by dayOfMonth asc")
    fun getLastSevenSessionsHours(currentMonth: Int, currentDayOfMonth: Int): LiveData<List<Float>>


    @Query("select hours from study_session_table where month= :monthSelected")
    fun getSessionHoursForMonth(monthSelected: Int): LiveData<List<Float>>


    /*
        Should we return live data straight form the dao? The repo methods can receive parameters that we can use to query the database
     */

    @Query("select * from study_session_table where date= :currentDate ")
    suspend fun getCurrentStudySession(currentDate: String): StudySession


    @Query("select * from study_session_table where month= :monthSelected")
    fun getAllSessionsWithMatchingMonth(monthSelected: Int): LiveData<List<StudySession>>


    /*
        To get the current week's study sessions query database for current day of week
     */

    @Query("select * from study_session_table where month= :currentMonth and dayOfMonth between :currentDayOfMonth - 6 and :currentDayOfMonth order by dayOfMonth asc")
    fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int): LiveData<List<StudySession>>

    @Query("select distinct year from study_session_table where year <= :currentYear order by year asc")
    fun getYearsWithSessions(currentYear: Int): LiveData<List<Int>>

    @Query("select  month from study_session_table where year = :yearSelected order by month asc")
    suspend fun getMonthsWithSelectedYear(yearSelected : Int) : List<Int>




    /*
        The problem is when the database is empty how do you know when to call updateStudySession or insert a study session
     */

    @Transaction
    suspend fun upsertStudySession(study: StudySession ){
        val inserted = insertStudySession(study)

        if(inserted == -1L){
            updateStudySession(study.month, study.dayOfMonth, study.hours)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudySession(study: StudySession): Long

    @Query("update study_session_table set hours = hours + :hours  where dayOfMonth= :currentDayOfMonth and month= :currentMonth")
    suspend fun updateStudySession(currentMonth: Int, currentDayOfMonth: Int, hours: Float )

}