package com.skittlemc.ritualforge.boss.abilities

class AbilityRegistry {

    private val abilities = mutableMapOf<String, Ability>()

    fun register(ability: Ability) {
        abilities[ability.id] = ability
    }

    fun get(id: String): Ability? = abilities[id]

    fun all(): Collection<Ability> = abilities.values
}
