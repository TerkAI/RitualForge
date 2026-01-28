package com.skittlemc.ritualforge.boss.phase

import com.skittlemc.ritualforge.api.events.BossPhaseChangeEvent
import com.skittlemc.ritualforge.boss.BossInstance
import org.bukkit.Bukkit

class PhaseController(private val boss: BossInstance) {

    var currentPhaseIndex: Int = 0
        private set

    fun tick() {
        val phases = boss.definition.phases
        if (phases.isEmpty()) return

        val healthPercent = boss.health.percent
        for (i in phases.indices.reversed()) {
            if (healthPercent <= phases[i].healthThreshold && i > currentPhaseIndex) {
                transition(i)
                break
            }
        }
    }

    private fun transition(newPhase: Int) {
        val previous = currentPhaseIndex
        currentPhaseIndex = newPhase

        Bukkit.getPluginManager().callEvent(
            BossPhaseChangeEvent(boss, previous, newPhase)
        )
    }

    fun currentPhaseAbilities(): List<String> {
        val phases = boss.definition.phases
        if (phases.isEmpty()) return emptyList()
        return phases.getOrNull(currentPhaseIndex)?.abilities ?: emptyList()
    }
}
