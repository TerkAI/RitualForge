package com.skittlemc.ritualforge.ritual.shrine

import org.bukkit.Location
import org.bukkit.Material

class ShrineDetector {

    data class DetectedShrine(
        val location: Location,
        val type: String
    )

    fun detect(location: Location): DetectedShrine? {
        val block = location.block
        if (block.type == Material.LODESTONE) {
            return DetectedShrine(location, "default")
        }
        // Additional multiblock detection can be added here
        return null
    }
}
