package com.skittlemc.ritualforge.content.boss

import org.bukkit.Material
import org.bukkit.entity.EntityType

data class BossDefinition(
    val id: String,
    val displayName: String,
    val description: String = "A fearsome boss.",
    val maxHealth: Double,
    val entityType: EntityType = EntityType.ZOMBIE,
    val skinTexture: String? = null,  // Player skin texture hash/URL
    val equipment: BossEquipment = BossEquipment(),
    val glowing: Boolean = false,
    val scale: Double = 1.0,
    val modelId: String? = null,
    val phases: List<PhaseDefinition>,
    val lootTableId: String?,
    val arenaRadius: Double = 30.0
)

data class BossEquipment(
    val mainHand: Material? = null,
    val offHand: Material? = null,
    val helmet: Material? = null,
    val chestplate: Material? = null,
    val leggings: Material? = null,
    val boots: Material? = null
)

data class PhaseDefinition(
    val index: Int,
    val healthThreshold: Double,
    val abilities: List<String>,
    val animation: String? = null
)
