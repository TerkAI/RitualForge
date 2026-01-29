package com.skittlemc.ritualforge.boss

import com.skittlemc.ritualforge.boss.combat.HealthComponent
import com.skittlemc.ritualforge.boss.display.BlockBossDisplay
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
    var blockDisplay: BlockBossDisplay? = null
    var target: Player? = null
    var alive: Boolean = true
        private set

    private var isMoving: Boolean = false

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
            blockDisplay?.teleportTo(e.location)

            // Check if entity is moving
            val velocity = e.velocity
            isMoving = velocity.lengthSquared() > 0.01
        }

        // Animate the block display
        blockDisplay?.let { display ->
            if (isMoving) {
                display.animateWalk()
            } else {
                display.animateIdle()
            }
        }

        phaseController.tick()
    }

    fun kill() {
        alive = false
        entity?.remove()
        entity = null
        blockDisplay?.remove()
        blockDisplay = null
    }
}
