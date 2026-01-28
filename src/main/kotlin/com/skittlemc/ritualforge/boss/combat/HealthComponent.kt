package com.skittlemc.ritualforge.boss.combat

class HealthComponent(val maxHealth: Double) {

    var current: Double = maxHealth
        private set

    val percent: Double get() = current / maxHealth
    val isDead: Boolean get() = current <= 0.0

    fun damage(amount: Double) {
        current = (current - amount).coerceAtLeast(0.0)
    }

    fun heal(amount: Double) {
        current = (current + amount).coerceAtMost(maxHealth)
    }

    fun reset() {
        current = maxHealth
    }
}
