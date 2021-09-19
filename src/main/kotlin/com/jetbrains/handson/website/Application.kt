package com.jetbrains.handson.website

import freemarker.cache.*
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.request.receiveParameters
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun giveShortTimeFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ISO_LOCAL_TIME
//  TODO:  return DateTimeFormatter.ofPattern("[HH:]mm:ss")
}

fun Application.module() {

    val svcClient = SvcClient

    install (FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        outputFormat = HTMLOutputFormat.INSTANCE
    }
    routing {
        static("/static") {
            resources("files")
        }
        get("/") {
            val entriesList = runBlocking {  svcClient.loadTimeEntries() }
            call.respond(FreeMarkerContent("index.ftl", mapOf("entries" to entriesList)))
        }
        post("/submit") {
            val params = call.receiveParameters()
            val problems = params["problems"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val measuredTime = params["measuredTime"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            var timeOfEntry = LocalDateTime.now()
            if (params["timeOfEntry"]?.isNotBlank()!!)
                timeOfEntry = LocalDateTime.parse(params["timeOfEntry"], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val newEntry = TimeEntry(UUID.randomUUID().toString(), listOf(problems), timeOfEntry, LocalTime.parse(measuredTime, giveShortTimeFormatter()))
            runBlocking {
                svcClient.postTimeEntry(newEntry)
            }
            timeEntries.add(0, newEntry)
            call.respondHtml {
                body {
                    h1 {
                        +"Thanks for submitting the entry!"
                    }
                    p {
                        +"We've submitted you new entry with following time "
                        b {
                            +newEntry.measuredTime.toString()
                        }
                    }
                    p {
                        +"You have submitted a total of XXX entries."
                    }
                    a("/") {
                        +"Go back"
                    }
                }
            }
        }
    }
}
