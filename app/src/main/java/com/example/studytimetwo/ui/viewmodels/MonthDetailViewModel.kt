package com.example.studytimetwo.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.studytimetwo.data.StudySession
import com.example.studytimetwo.data.repository.StudyRepository
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
class MonthDetailViewModel @ViewModelInject constructor(
    private val repository: StudyRepository,
    @Assisted savedStateHandle: SavedStateHandle

) : ViewModel() {


    private val monthDayLabels = arrayListOf<String>(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31"
    )
    private val months = arrayListOf<String>(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    private var monthSelected = 0

    init {
        monthSelected = savedStateHandle.get<Int>(MONTH_SELECTED)!!
    }



    private val _monthsStudySessionHours = repository.getSessionHoursForMonth(monthSelected)
    private val _monthsStudySessions = Transformations.switchMap(_monthsStudySessionHours) {
        repository.getAllSessionsWithMatchingMonth(monthSelected)
    }
    val monthBarData = Transformations.map(_monthsStudySessions) {
        setSessionWithMonthBarData(it)
    }

    var month: String = ""

    fun setSessionWithMonthBarData(monthsStudySessionList: List<StudySession>): BarData {

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

    companion object {
        const val MONTH_SELECTED = "month_selected"
    }
}

