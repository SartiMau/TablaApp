package com.example.tablaapp.util

import java.util.Calendar

const val EMPTY_STRING = ""
const val ONE_INT = 1

private val listOfMonth =
    listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

fun getMonth(): String {
    val calendar = Calendar.getInstance()
    return listOfMonth[calendar.get(Calendar.MONTH)]
}
