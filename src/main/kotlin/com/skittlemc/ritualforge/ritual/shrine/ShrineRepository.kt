package com.skittlemc.ritualforge.ritual.shrine

import org.bukkit.Location

class ShrineRepository {

    private val shrines = mutableMapOf<String, RegisteredShrine>()

    data class RegisteredShrine(
        val id: String,
        val location: Location,
        val type: String,
        var cooldownUntil: Long = 0L
    )

    fun register(shrine: RegisteredShrine) {
        shrines[shrine.id] = shrine
    }

    fun unregister(id: String) {
        shrines.remove(id)
    }

    fun get(id: String): RegisteredShrine? = shrines[id]

    fun findNear(location: Location, radius: Double): RegisteredShrine? {
        val radiusSq = radius * radius
        return shrines.values.firstOrNull {
            it.location.world == location.world &&
            it.location.distanceSquared(location) <= radiusSq
        }
    }

    fun all(): Collection<RegisteredShrine> = shrines.values

    fun isOnCooldown(id: String): Boolean {
        val shrine = shrines[id] ?: return false
        return System.currentTimeMillis() < shrine.cooldownUntil
    }
}
