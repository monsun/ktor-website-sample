package com.jetbrains.handson.website

import java.time.LocalDateTime
import java.time.LocalTime

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
val timeEntries = mutableListOf(TimeEntry(UUID.randomUUID().toString(), listOf("Full course yellow"), LocalDateTime.now(), LocalTime.of(0, 10, 0)))

@Serializable
data class TimeEntry (val id:String, val problems: List<String>, val timeOfEntry: LocalDateTime = LocalDateTime.now(), val measuredTime: LocalTime)