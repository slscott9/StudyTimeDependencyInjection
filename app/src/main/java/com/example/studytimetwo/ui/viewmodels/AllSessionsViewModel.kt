package com.example.studytimetwo.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studytimetwo.data.repository.StudyRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AllSessionsViewModel @ViewModelInject constructor(
    private val repository: StudyRepository,

) : ViewModel(){

    val yearList = repository.getYearsWithSessions(LocalDateTime.now().year)

    private val _monthsFromSelectedYear = MutableLiveData<List<Int>>()
    var monthsFromSelectedYear: LiveData<List<Int>> = _monthsFromSelectedYear


    fun getMonthsWithSelectedYear(selectedYear: Int){
        viewModelScope.launch {
            _monthsFromSelectedYear.value = repository.getMonthWithSelectedYear(selectedYear)
        }
    }

}