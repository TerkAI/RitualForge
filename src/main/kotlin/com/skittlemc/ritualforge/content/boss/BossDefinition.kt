package com.skittlemc.ritualforge.content.boss

import com.skittlemc.ritualforge.content.model.ModelDefinition

data class BossDefinition(
    val id: String,
    val displayName: String,
    val maxHealth: Double,
    val modelId: String,
    val phases: List<PhaseDefinition>,
    val lootTableId: String?,
    val arenaRadius: Double = 30.0
)

data class PhaseDefinition(
    val index: Int,
    val healthThreshold: Double,
    val abilities: List<String>,
    val animation: String? = null
)
