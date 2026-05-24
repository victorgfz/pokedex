@androidx.room.Entity(tableName = "TeamEntity")
data class TeamEntity(
    @androidx.room.PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val capturedLocation: String, // Novo campo obrigatório
    val addedAt: Long = java.lang.System.currentTimeMillis()
)