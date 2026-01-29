package com.skittlemc.ritualforge.boss.abilities

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle

class Telegraph(
    private val location: Location,
    private val radius: Double,
    private val durationTicks: Int,
    private val color: Color = Color.RED
) {
    private var ticksRemaining = durationTicks

    val isActive: Boolean get() = ticksRemaining > 0

    fun tick() {
        if (!isActive) return
        ticksRemaining--
        spawnParticles()
    }

    private fun spawnParticles() {
        val world = location.world ?: return
        val dustOptions = Particle.DustOptions(color, 1.5f)
        val steps = 32
        for (i in 0 until steps) {
            val angle = 2.0 * Math.PI * i / steps
            val x = location.x + radius * Math.cos(angle)
            val z = location.z + radius * Math.sin(angle)
            world.spawnParticle(Particle.REDSTONE, x, location.y + 0.1, z, 1, 0.0, 0.0, 0.0, 0.0, dustOptions)
        }
    }
}
