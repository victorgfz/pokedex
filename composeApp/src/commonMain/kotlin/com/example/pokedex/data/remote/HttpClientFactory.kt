package com.pokedex.app.data.remote

import io.ktor.client.HttpClient

internal expect fun buildHttpClient(): HttpClient