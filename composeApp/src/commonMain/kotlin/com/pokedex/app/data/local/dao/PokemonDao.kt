package com.pokedex.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pokedex.app.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_cache ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPagedPokemons(limit: Int, offset: Int): List<PokemonEntity>

    @Query("SELECT COUNT(*) FROM pokemon_cache")
    suspend fun getCount(): Int

    @Query("""
    SELECT * FROM pokemon_cache 
    WHERE (:name IS NULL OR name LIKE '%' || :name || '%') 
    AND (:type IS NULL OR types LIKE '%' || :type || '%')
    ORDER BY id ASC
    LIMIT :limit OFFSET :offset
""")
    suspend fun getFilteredPokemons(
        limit: Int,
        offset: Int,
        name: String?,
        type: String?
    ): List<PokemonEntity>
}
