package com.pokedex.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pokedex.app.data.local.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamMember(member: TeamEntity)

    @Query("DELETE FROM team WHERE id = :pokemonId")
    suspend fun removeTeamMember(pokemonId: Int)

    @Query("SELECT * FROM team ORDER BY name ASC")
    fun getAllTeamMembers(): Flow<List<TeamEntity>>

    @Query("SELECT COUNT(*) FROM team")
    suspend fun getTeamCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM team WHERE id = :pokemonId)")
    suspend fun isInTeam(pokemonId: Int): Boolean
}