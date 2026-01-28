package com.skittlemc.ritualforge.boss.behavior.movement

import org.bukkit.Location
import kotlin.math.cos
import kotlin.math.sin

class StrafeMovement(
    private val speed: Double = 0.15,
    private val radius: Double = 5.0
) {
    private var angle: Double = 0.0

    fun computeNext(center: Location, target: Location): Location {
        angle += speed
        val offsetX = cos(angle) * radius
        val offsetZ = sin(angle) * radius
        return target.clone().add(offsetX, 0.0, offsetZ)
    }
}
