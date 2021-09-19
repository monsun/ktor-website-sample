package com.jetbrains.handson.website

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


object SvcClient {

    //TODO: move to config
    const val URL = "http://localhost:8080"

    fun newClient() = HttpClient() {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = JacksonSerializer() {
                val timeModule = JavaTimeModule()
                timeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME))
                timeModule.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME))
                timeModule.addSerializer(LocalTime::class.java, LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME))
                registerModule(timeModule)
                serializationConfig.without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
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

    fun postTimeEntry(entry: TimeEntry) {
        runBlocking <Unit> {
            newClient().use {
                client -> client.post("$URL/timeEntry") {
                    contentType(ContentType.Application.Json)
                    body = entry
                }
            }
        }

    }

}