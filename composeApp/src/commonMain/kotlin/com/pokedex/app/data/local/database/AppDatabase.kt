package com.pokedex.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

fun getDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .build()
}
