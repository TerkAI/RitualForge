package com.skittlemc.ritualforge.gui

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.entity.Player

class GuiManager(private val plugin: RitualForgePlugin) {

    private val bossListGui = BossListGui(plugin)
    private val bossInfoGui = BossInfoGui(plugin)

    fun openBossList(player: Player) {
        player.openInventory(bossListGui.create(player))
    }

    fun openBossInfo(player: Player, bossId: String) {
        val inventory = bossInfoGui.create(player, bossId)
        if (inventory != null) {
            player.openInventory(inventory)
        }
    }
}
