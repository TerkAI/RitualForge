package com.skittlemc.ritualforge.gui

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

class GuiListener(private val plugin: RitualForgePlugin) : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder
        if (holder !is GuiHolder) return

        event.isCancelled = true

        val player = event.whoClicked as? Player ?: return
        val clickedItem = event.currentItem ?: return
        if (clickedItem.type == Material.AIR) return

        when (holder.guiType) {
            GuiType.BOSS_LIST -> handleBossListClick(player, event.slot, clickedItem)
            GuiType.BOSS_INFO -> handleBossInfoClick(player, holder, event.slot)
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val holder = event.inventory.holder
        if (holder is GuiHolder) {
            event.isCancelled = true
        }
    }

    private fun handleBossListClick(player: Player, slot: Int, clickedItem: org.bukkit.inventory.ItemStack) {
        if (clickedItem.type == Material.BOOK) return

        val bosses = plugin.contentManager.getAllBossDefinitions()
        if (slot < bosses.size) {
            val boss = bosses[slot]
            player.closeInventory()
            plugin.guiManager.openBossInfo(player, boss.id)
        }
    }

    private fun handleBossInfoClick(player: Player, holder: GuiHolder, slot: Int) {
        if (slot == 22) {
            player.closeInventory()
            plugin.guiManager.openBossList(player)
        }
    }
}
