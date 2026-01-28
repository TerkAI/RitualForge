package com.skittlemc.ritualforge.boss.behavior

import com.skittlemc.ritualforge.boss.BossInstance
import com.skittlemc.ritualforge.boss.abilities.AbilityRegistry

class BehaviorController(
    private val boss: BossInstance,
    private val targeting: TargetingSystem,
    private val abilityRegistry: AbilityRegistry
) {
    private var cooldownTicks: Int = 0

    fun tick() {
        boss.target = targeting.selectTarget(boss)

        if (cooldownTicks > 0) {
            cooldownTicks--
            return
        }

        val abilities = boss.phaseController.currentPhaseAbilities()
        if (abilities.isEmpty()) return

        val abilityId = abilities.random()
        val ability = abilityRegistry.get(abilityId) ?: return

        val target = boss.target ?: return
        val context = com.skittlemc.ritualforge.boss.abilities.AbilityContext(boss, target)

        if (ability.canUse(context)) {
            ability.execute(context)
            cooldownTicks = ability.cooldownTicks
        }
    }
}
