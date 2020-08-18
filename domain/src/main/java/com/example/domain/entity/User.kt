package com.example.domain.entity

import com.example.domain.util.EMPTY_STRING
import com.example.domain.util.ZERO_POINT

data class User(
    val name: String = EMPTY_STRING,
    var points: Int = ZERO_POINT
)