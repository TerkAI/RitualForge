package com.skittlemc.ritualforge.boss.behavior.movement

import org.bukkit.Location

class FollowMovement(
    private val speed: Double = 0.2
) {
    fun computeNext(current: Location, target: Location): Location {
        val direction = target.toVector().subtract(current.toVector())
        if (direction.lengthSquared() < 0.01) return current

        direction.normalize().multiply(speed)
        return current.clone().add(direction)
    }
}
