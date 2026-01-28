package com.skittlemc.ritualforge.boss.behavior.movement

import org.bukkit.Location
import org.bukkit.util.Vector

class LeapMovement(
    private val horizontalPower: Double = 1.5,
    private val verticalPower: Double = 0.8
) {
    fun computeLeapVelocity(from: Location, to: Location): Vector {
        val direction = to.toVector().subtract(from.toVector())
        direction.y = 0.0
        if (direction.lengthSquared() > 0.0) direction.normalize()
        direction.multiply(horizontalPower)
        direction.y = verticalPower
        return direction
    }
}
