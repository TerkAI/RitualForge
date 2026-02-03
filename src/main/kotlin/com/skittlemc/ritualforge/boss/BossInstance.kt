package com.skittlemc.ritualforge.boss

import com.skittlemc.ritualforge.boss.combat.HealthComponent
import com.skittlemc.ritualforge.boss.display.DBModelDisplay
import com.skittlemc.ritualforge.boss.phase.PhaseController
import com.skittlemc.ritualforge.content.boss.BossDefinition
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.UUID

class BossInstance(
    val uuid: UUID = UUID.randomUUID(),
    val definition: BossDefinition,
    val spawnLocation: Location,
    val arena: Arena
) {
    val health = HealthComponent(definition.maxHealth)
    val phaseController = PhaseController(this)

    var entity: LivingEntity? = null
    var modelDisplay: DBModelDisplay? = null
    var target: Player? = null
    var alive: Boolean = true
        private set

    val currentPhase: Int get() = phaseController.currentPhaseIndex

    fun tick() {
        if (!alive) return

        // Sync health from entity
        entity?.let { e ->
            if (e.isDead) {
                kill()
                return
            }
            health.current = e.health

            // Update display position to follow entity
            modelDisplay?.teleportTo(e.location)
        }

        phaseController.tick()
    }

    fun kill() {
        alive = false
        entity?.remove()
        entity = null
        modelDisplay?.remove()
        modelDisplay = null
    }
}
