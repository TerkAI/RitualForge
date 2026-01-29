package com.skittlemc.ritualforge.content

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.content.boss.BossDefinition
import com.skittlemc.ritualforge.content.boss.BossLoader
import com.skittlemc.ritualforge.content.loot.LootLoader
import com.skittlemc.ritualforge.content.loot.LootTableDefinition
import com.skittlemc.ritualforge.content.model.ModelLoader
import com.skittlemc.ritualforge.content.ritual.RitualDefinition
import com.skittlemc.ritualforge.content.ritual.RitualLoader
import com.skittlemc.ritualforge.content.ritual.ShrineDefinition
import com.skittlemc.ritualforge.content.ritual.ShrineLoader

class ContentManager(private val plugin: RitualForgePlugin) {

    private val bosses = mutableMapOf<String, BossDefinition>()
    private val rituals = mutableMapOf<String, RitualDefinition>()
    private val shrines = mutableMapOf<String, ShrineDefinition>()
    private val lootTables = mutableMapOf<String, LootTableDefinition>()

    private val bossLoader = BossLoader(plugin)
    private val ritualLoader = RitualLoader(plugin)
    private val shrineLoader = ShrineLoader(plugin)
    private val lootLoader = LootLoader(plugin)
    private val modelLoader = ModelLoader(plugin)

    fun loadAll() {
        bosses.clear()
        rituals.clear()
        shrines.clear()
        lootTables.clear()

        bossLoader.load().forEach { bosses[it.id] = it }
        ritualLoader.load().forEach { rituals[it.id] = it }
        shrineLoader.load().forEach { shrines[it.id] = it }
        lootLoader.load().forEach { lootTables[it.id] = it }
        modelLoader.load()

        plugin.logger.info("Loaded ${bosses.size} bosses, ${rituals.size} rituals, ${shrines.size} shrines, ${lootTables.size} loot tables.")
    }

    fun getBossDefinition(id: String): BossDefinition? = bosses[id]
    fun getRitualDefinition(id: String): RitualDefinition? = rituals[id]
    fun getShrineDefinition(id: String): ShrineDefinition? = shrines[id]
    fun getLootTable(id: String): LootTableDefinition? = lootTables[id]

    fun getAllBossDefinitions(): List<BossDefinition> = bosses.values.toList()
    fun getAllRitualDefinitions(): List<RitualDefinition> = rituals.values.toList()
    fun getAllShrineDefinitions(): List<ShrineDefinition> = shrines.values.toList()

    fun getRitualForBoss(bossId: String): RitualDefinition? {
        return rituals.values.firstOrNull { it.bossId == bossId }
    }
}
