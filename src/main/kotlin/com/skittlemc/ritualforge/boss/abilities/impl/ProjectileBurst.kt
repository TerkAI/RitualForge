package com.skittlemc.ritualforge.boss.abilities.impl

import com.skittlemc.ritualforge.boss.abilities.Ability
import com.skittlemc.ritualforge.boss.abilities.AbilityContext
import org.bukkit.Sound
import org.bukkit.entity.SmallFireball
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ProjectileBurst(
    private val count: Int = 8,
    private val speed: Double = 0.5
) : Ability {

    override val id: String = "projectile_burst"
    override val cooldownTicks: Int = 100

    override fun canUse(context: AbilityContext): Boolean = true

    override fun execute(context: AbilityContext) {
        val origin = context.boss.spawnLocation
        val world = origin.world ?: return

        world.playSound(origin, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.6f)

        for (i in 0 until count) {
            val angle = 2.0 * Math.PI * i / count
            val direction = Vector(cos(angle) * speed, 0.1, sin(angle) * speed)

            world.spawn(origin.clone().add(0.0, 1.5, 0.0), SmallFireball::class.java) { fireball ->
                fireball.direction = direction
                fireball.yield = 0f
            }
        }
    }
}
