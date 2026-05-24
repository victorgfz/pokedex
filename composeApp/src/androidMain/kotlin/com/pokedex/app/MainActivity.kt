package com.pokedex.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pokedex.app.data.local.database.AppDatabase
import com.pokedex.app.data.local.database.appContext
import com.pokedex.app.data.remote.PokeApiService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContext = applicationContext

        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}