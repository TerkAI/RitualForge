package com.skittlemc.ritualforge.boss

import org.bukkit.Location

data class Arena(
    val center: Location,
    val radius: Double
) {
    fun contains(location: Location): Boolean {
        if (location.world != center.world) return false
        return location.distanceSquared(center) <= radius * radius
    }

    fun clamp(location: Location): Location {
        if (contains(location)) return location
        val direction = location.toVector().subtract(center.toVector())
        direction.normalize().multiply(radius)
        return center.clone().add(direction)
    }
}
