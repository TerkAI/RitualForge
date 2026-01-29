package com.skittlemc.ritualforge.gui

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.util.Text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class BossListGui(private val plugin: RitualForgePlugin) {

    fun create(player: Player): Inventory {
        val bosses = plugin.contentManager.getAllBossDefinitions()
        val rows = ((bosses.size + 8) / 9).coerceIn(1, 6)
        val holder = GuiHolder(GuiType.BOSS_LIST)
        val inventory = Bukkit.createInventory(holder, rows * 9, Text.parse("<dark_purple><bold>Boss Compendium"))
        holder.setInventory(inventory)

        if (bosses.isEmpty()) {
            inventory.setItem(4, createInfoItem())
        } else {
            bosses.forEachIndexed { index, boss ->
                if (index < 54) {
                    inventory.setItem(index, createBossItem(boss.id, boss.displayName, boss.description))
                }
            }
        }

        return inventory
    }

    private fun createInfoItem(): ItemStack {
        val item = ItemStack(Material.BOOK)
        val meta = item.itemMeta
        meta.displayName(Text.parse("<yellow>No Bosses Loaded"))
        meta.lore(listOf(
            Text.parse("<gray>Add boss YAML files to the"),
            Text.parse("<gray><white>plugins/RitualForge/bosses/<gray> folder"),
            Text.parse("<gray>and reload the plugin.")
        ))
        item.itemMeta = meta
        return item
    }

    private fun createBossItem(id: String, displayName: String, description: String): ItemStack {
        val item = ItemStack(Material.WITHER_SKELETON_SKULL)
        val meta = item.itemMeta
        meta.displayName(Text.parse("<red><bold>$displayName").decoration(TextDecoration.ITALIC, false))
        meta.lore(listOf(
            Text.parse("<dark_gray>$id"),
            Text.parse(""),
            Text.parse("<gray>$description"),
            Text.parse(""),
            Text.parse("<yellow>Click for spawn info")
        ).map { it.decoration(TextDecoration.ITALIC, false) })

        meta.setCustomModelData(id.hashCode())
        item.itemMeta = meta
        return item
    }
}
