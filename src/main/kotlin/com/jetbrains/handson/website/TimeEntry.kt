package com.jetbrains.handson.website

val timeEntries = mutableListOf(TimeEntry("Header1","Body 1 Lorem ipsum"))

data class TimeEntry (val headline: String, val body: String)