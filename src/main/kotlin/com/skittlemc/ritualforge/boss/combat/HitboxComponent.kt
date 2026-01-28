package com.skittlemc.ritualforge.boss.combat

import org.bukkit.Location
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

class HitboxComponent(
    private val halfWidth: Double = 0.6,
    private val height: Double = 2.0
) {
    fun getBoundingBox(location: Location): BoundingBox {
        val min = location.toVector().subtract(Vector(halfWidth, 0.0, halfWidth))
        val max = location.toVector().add(Vector(halfWidth, height, halfWidth))
        return BoundingBox.of(min, max)
    }

    fun intersects(location: Location, point: Location): Boolean {
        return getBoundingBox(location).contains(point.toVector())
    }
}
