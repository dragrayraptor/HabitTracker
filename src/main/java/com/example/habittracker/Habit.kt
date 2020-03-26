package com.example.habittracker

import java.io.Serializable

enum class Priority {
    High, Medium, Low
}

enum class HabitType {
    Bad, Good
}

class Habit(val title: String, val description: String, val priority: Priority,
            val type: HabitType, val count: Int, val periodicity: Int): Serializable
