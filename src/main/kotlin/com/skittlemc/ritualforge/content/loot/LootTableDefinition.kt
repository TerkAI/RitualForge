package com.skittlemc.ritualforge.content.loot

import org.bukkit.Material

data class LootTableDefinition(
    val id: String,
    val entries: List<LootEntry>
)

data class LootEntry(
    val material: Material,
    val amount: IntRange = 1..1,
    val weight: Double = 1.0,
    val customModelData: Int? = null,
    val displayName: String? = null
)
