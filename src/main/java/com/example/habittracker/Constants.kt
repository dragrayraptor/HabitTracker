package com.example.habittracker

const val CREATE_CODE = 0
const val CHANGE_CODE = 1

const val HABIT = "habit"

val Priorities = listOf(
    "Низкий",
    "Средний",
    "Высокий"
)

val priorityToText = mapOf<Priority, String>(
    Priority.High to Priorities[2],
    Priority.Medium to Priorities[1],
    Priority.Low to Priorities[0]
)