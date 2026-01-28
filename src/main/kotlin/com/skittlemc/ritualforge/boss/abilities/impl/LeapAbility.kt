package com.skittlemc.ritualforge.boss.abilities.impl

import com.skittlemc.ritualforge.boss.abilities.Ability
import com.skittlemc.ritualforge.boss.abilities.AbilityContext
import com.skittlemc.ritualforge.boss.behavior.movement.LeapMovement

class LeapAbility(
    private val damage: Double = 8.0,
    private val minRange: Double = 5.0
) : Ability {

    override val id: String = "leap"
    override val cooldownTicks: Int = 80

    private val leapMovement = LeapMovement()

    override fun canUse(context: AbilityContext): Boolean {
        return context.boss.spawnLocation.distance(context.target.location) >= minRange
    }

    override fun execute(context: AbilityContext) {
        val velocity = leapMovement.computeLeapVelocity(
            context.boss.spawnLocation,
            context.target.location
        )
        // In a full implementation, this velocity would be applied to the boss entity
        // and impact damage dealt on landing
    }
}
