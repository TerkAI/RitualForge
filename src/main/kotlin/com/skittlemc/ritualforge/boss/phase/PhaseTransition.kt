package com.skittlemc.ritualforge.boss.phase

data class PhaseTransition(
    val fromPhase: Int,
    val toPhase: Int,
    val animationId: String? = null,
    val invulnerableTicks: Int = 0
)
