package com.example.tablaapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel @ViewModelInject constructor() : ViewModel() {

    private var mutableMainState: MutableLiveData<MainData> = MutableLiveData()
    val mainState: LiveData<MainData>
        get() = mutableMainState

    data class MainData(val status: MainStatus)

    enum class MainStatus
}