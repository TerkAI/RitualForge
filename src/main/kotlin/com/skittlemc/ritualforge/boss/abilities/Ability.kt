package com.skittlemc.ritualforge.boss.abilities

interface Ability {
    val id: String
    val cooldownTicks: Int

    fun canUse(context: AbilityContext): Boolean
    fun execute(context: AbilityContext)
}
