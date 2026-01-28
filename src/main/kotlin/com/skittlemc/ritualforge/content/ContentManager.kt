package com.skittlemc.ritualforge.content

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.content.boss.BossDefinition
import com.skittlemc.ritualforge.content.boss.BossLoader
import com.skittlemc.ritualforge.content.loot.LootLoader
import com.skittlemc.ritualforge.content.loot.LootTableDefinition
import com.skittlemc.ritualforge.content.model.ModelLoader
import com.skittlemc.ritualforge.content.ritual.RitualDefinition
import com.skittlemc.ritualforge.content.ritual.RitualLoader

class ContentManager(private val plugin: RitualForgePlugin) {

    private val bosses = mutableMapOf<String, BossDefinition>()
    private val rituals = mutableMapOf<String, RitualDefinition>()
    private val lootTables = mutableMapOf<String, LootTableDefinition>()

    private val bossLoader = BossLoader(plugin)
    private val ritualLoader = RitualLoader(plugin)
    private val lootLoader = LootLoader(plugin)
    private val modelLoader = ModelLoader(plugin)

    fun loadAll() {
        bosses.clear()
        rituals.clear()
        lootTables.clear()

        bossLoader.load().forEach { bosses[it.id] = it }
        ritualLoader.load().forEach { rituals[it.id] = it }
        lootLoader.load().forEach { lootTables[it.id] = it }
        modelLoader.load()

        plugin.logger.info("Loaded ${bosses.size} bosses, ${rituals.size} rituals, ${lootTables.size} loot tables.")
    }

    fun getBossDefinition(id: String): BossDefinition? = bosses[id]
    fun getRitualDefinition(id: String): RitualDefinition? = rituals[id]
    fun getLootTable(id: String): LootTableDefinition? = lootTables[id]

    fun allBossDefinitions(): Collection<BossDefinition> = bosses.values
    fun allRitualDefinitions(): Collection<RitualDefinition> = rituals.values
}
