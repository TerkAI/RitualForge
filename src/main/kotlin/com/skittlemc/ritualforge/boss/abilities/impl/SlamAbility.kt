package com.skittlemc.ritualforge.boss.abilities.impl

import com.skittlemc.ritualforge.boss.abilities.Ability
import com.skittlemc.ritualforge.boss.abilities.AbilityContext
import com.skittlemc.ritualforge.boss.abilities.Telegraph
import org.bukkit.Color
import org.bukkit.Sound
import org.bukkit.entity.Player

class SlamAbility(
    private val damage: Double = 12.0,
    private val radius: Double = 4.0,
    private val telegraphTicks: Int = 30
) : Ability {

    override val id: String = "slam"
    override val cooldownTicks: Int = 60

    override fun canUse(context: AbilityContext): Boolean {
        val dist = context.boss.spawnLocation.distance(context.target.location)
        return dist <= radius * 2
    }

    override fun execute(context: AbilityContext) {
        val location = context.boss.spawnLocation
        val telegraph = Telegraph(location, radius, telegraphTicks, Color.RED)

        val world = location.world ?: return
        world.playSound(location, Sound.ENTITY_RAVAGER_ROAR, 1.0f, 0.8f)

        world.getNearbyEntities(location, radius, radius, radius)
            .filterIsInstance<Player>()
            .forEach { player ->
                player.damage(damage)
            }
    }
}
