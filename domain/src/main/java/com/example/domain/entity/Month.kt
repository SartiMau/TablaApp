package com.example.domain.entity

import com.example.domain.util.EMPTY_STRING
import com.example.domain.util.ZERO_POINT

data class Month(
    val month: String = EMPTY_STRING,
    val winnerName: String = EMPTY_STRING,
    val winnerPoint: Int = ZERO_POINT
)
