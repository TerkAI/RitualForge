package com.skittlemc.ritualforge.boss.combat

import com.skittlemc.ritualforge.boss.BossInstance

class DamageRouter {

    fun route(boss: BossInstance, rawDamage: Double): Double {
        val finalDamage = applyModifiers(boss, rawDamage)
        boss.health.damage(finalDamage)
        return finalDamage
    }

    private fun applyModifiers(boss: BossInstance, rawDamage: Double): Double {
        // Phase-based damage reduction, armor, etc. can be added here
        return rawDamage
    }
}
