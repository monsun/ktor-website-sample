package com.jetbrains.handson.website

import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.text.DateFormat
import java.text.DateFormat.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME


object SvcClient {

    //TODO: move to config
    const val URL = "http://localhost:8080"

    fun newClient() = HttpClient() {
        install(Logging)
        install(JsonFeature) {
            serializer = JacksonSerializer() {
                val timeModule = JavaTimeModule()
                timeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME))
                registerModule(timeModule)
            }
        }
    }

    fun loadTimeEntries(): List<TimeEntry> {
        val timeEntries: List<TimeEntry> = runBlocking {
            newClient().use {
                    client -> client.get("$URL/timeEntry")
            }
        }
        return timeEntries
    }

}