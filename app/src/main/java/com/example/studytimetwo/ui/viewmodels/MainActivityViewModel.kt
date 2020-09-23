package com.example.studytimetwo.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.studytimetwo.data.StudySession
import com.example.studytimetwo.data.repository.StudyRepository
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainActivityViewModel @ViewModelInject constructor(
    private val repository: StudyRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(){


    private val monthDayLabels = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
    private val nullLabels = arrayListOf<String>("No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data")
    private val months = arrayListOf<String>("January", "February" ,"March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    var month: String = ""
    private  var currentMonth = 0
    private var currentDayOfMonth = 0


    init {
        currentMonth = LocalDateTime.now().monthValue
        currentDayOfMonth = LocalDateTime.now().dayOfMonth
    }

    /*
        When _lastSevenSessionsHours changes get the last sevenStudySessions, use date and hours properties to transform into bar data.

        Activities observe bar data and update bar charts
     */
    private val _lastSevenSessionsHours = repository.getLastSevenSessionsHours(currentMonth, currentDayOfMonth)
    private val lastSevenStudySessions : LiveData<List<StudySession>> = Transformations.switchMap(_lastSevenSessionsHours){
        repository.getLastSevenSessions(currentMonth, currentDayOfMonth)
    }
    val weekBarData: LiveData<BarData> = Transformations.map(lastSevenStudySessions){
        setLastSevenSessionsBarData(it)
    }

    private val _monthsStudySessionHours = repository.getSessionHoursForMonth(currentMonth)
    private val _monthsStudySessions = Transformations.switchMap(_monthsStudySessionHours){
        repository.getAllSessionsWithMatchingMonth(currentMonth)
    }
    val monthBarData = Transformations.map(_monthsStudySessions){
        setSessionWithMonthBarData(it)
    }

    fun setSessionWithMonthBarData(monthsStudySessionList: List<StudySession>) : BarData {

        val monthBarDataSetValues = MutableList(31) { BarEntry(0F, 0) }
        var monthBarData = BarData()

        if (monthsStudySessionList.isNullOrEmpty()) {
            val barDataSet = BarDataSet(monthBarDataSetValues, "Hours")
            monthBarData = BarData(monthDayLabels, barDataSet)

        } else {
            //Entries uses the fixed size so we can add values to it at specific indexes
            //BarEntry(value, index) we can specify the index this bar value will be placed

            for (i in monthsStudySessionList.indices) {
                monthBarDataSetValues[monthsStudySessionList[i].dayOfMonth - 1] =
                    BarEntry(
                        monthsStudySessionList[i].hours,
                        monthsStudySessionList[i].dayOfMonth - 1
                    ) //to match the array indexes
            }

            val monthBarDataSet = BarDataSet(monthBarDataSetValues, "Hours")
            month =
                months[monthsStudySessionList[0].month - 1] //set the month value to be displayed in the monthBarChart's description

            monthBarData = BarData(monthDayLabels, monthBarDataSet)
        }

        return monthBarData
    }


    fun setLastSevenSessionsBarData(list: List<StudySession>): BarData {
        val weekBarDataSetValues = ArrayList<BarEntry>()
        var weekBarData = BarData()

        if (list.isNullOrEmpty()) {
            val barDataSet = BarDataSet(weekBarDataSetValues, "Sessions")
            weekBarData = BarData(nullLabels, barDataSet)

        } else {
            val datesFromSessions = ArrayList<String>()

            for (session in list.indices) {
                weekBarDataSetValues.add(
                    BarEntry(
                        list[session].hours,
                        session
                    )
                )
                datesFromSessions.add(list[session].date)
            }
            val weekBarDataSet = BarDataSet(weekBarDataSetValues, "Hours")
            weekBarData = BarData(datesFromSessions, weekBarDataSet)
        }

        return weekBarData
    }

    fun upsertStudySession(newStudySession: StudySession){
        viewModelScope.launch {
            repository.insertStudySession(newStudySession)
        }
    }


}