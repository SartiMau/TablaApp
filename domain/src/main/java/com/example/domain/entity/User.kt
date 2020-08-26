package com.example.domain.entity

import com.example.domain.util.ZERO_POINT

data class User(
    val name: String,
    var points: Int = ZERO_POINT
)
