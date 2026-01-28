package com.skittlemc.ritualforge.ritual.shrine

import com.skittlemc.ritualforge.content.ritual.BlockOffset
import org.bukkit.Location

class MultiblockPattern(
    private val offsets: List<BlockOffset>
) {
    fun matches(origin: Location): Boolean {
        return offsets.all { offset ->
            val check = origin.clone().add(
                offset.dx.toDouble(),
                offset.dy.toDouble(),
                offset.dz.toDouble()
            )
            check.block.type == offset.material
        }
    }
}
