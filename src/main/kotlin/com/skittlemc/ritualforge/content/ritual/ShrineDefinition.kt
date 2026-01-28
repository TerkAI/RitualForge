package com.skittlemc.ritualforge.content.ritual

import org.bukkit.Material

data class ShrineDefinition(
    val id: String,
    val coreBlock: Material = Material.LODESTONE,
    val multiblock: List<BlockOffset>? = null
)

data class BlockOffset(
    val dx: Int,
    val dy: Int,
    val dz: Int,
    val material: Material
)
