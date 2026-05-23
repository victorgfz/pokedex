package com.pokedex.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal actual fun buildHttpClient(): HttpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true; prettyPrint = false })
    }
    install(Logging) {
        level = LogLevel.NONE
    }
    engine {
        connectTimeout = 30_000
        socketTimeout  = 30_000
    }
}