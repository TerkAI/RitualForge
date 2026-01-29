package com.skittlemc.ritualforge.gui

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.util.Text
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class BossInfoGui(private val plugin: RitualForgePlugin) {

    fun create(player: Player, bossId: String): Inventory? {
        val boss = plugin.contentManager.getBossDefinition(bossId) ?: return null
        val ritual = plugin.contentManager.getRitualForBoss(bossId)

        val holder = GuiHolder(GuiType.BOSS_INFO, mapOf("bossId" to bossId))
        val inventory = Bukkit.createInventory(holder, 27, Text.parse("<dark_red><bold>${boss.displayName}"))
        holder.setInventory(inventory)

        // Boss skull in center
        inventory.setItem(4, createBossHead(boss.displayName, boss.description))

        // Stats on the left
        inventory.setItem(10, createStatItem(Material.RED_DYE, "Health", "${boss.maxHealth} HP"))
        inventory.setItem(11, createStatItem(Material.DIAMOND_SWORD, "Phases", "${boss.phases.size} phases"))
        inventory.setItem(12, createStatItem(Material.ENDER_EYE, "Arena", "${boss.arenaRadius} block radius"))

        // Spawn info on the right
        if (ritual != null) {
            val shrine = plugin.contentManager.getShrineDefinition(ritual.shrineId)
            inventory.setItem(14, createRitualItem(ritual.id, ritual.catalystItem))
            inventory.setItem(15, createShrineItem(shrine?.id ?: "unknown", shrine?.coreBlock ?: Material.LODESTONE))
            inventory.setItem(16, createChannelingItem(ritual.channelingTicks))
        } else {
            inventory.setItem(15, createNoRitualItem())
        }

        // Back button
        inventory.setItem(22, createBackButton())

        return inventory
    }

    private fun createBossHead(name: String, description: String): ItemStack {
        val item = ItemStack(Material.WITHER_SKELETON_SKULL)
        val meta = item.itemMeta
        meta.displayName(Text.parse("<red><bold>$name").decoration(TextDecoration.ITALIC, false))
        meta.lore(listOf(
            Text.parse("<gray>$description")
        ).map { it.decoration(TextDecoration.ITALIC, false) })
        item.itemMeta = meta
        return item
    }

    private fun createStatItem(material: Material, label: String, value: String): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta.displayName(Text.parse("<gold>$label").decoration(TextDecoration.ITALIC, false))
        meta.lore(listOf(
            Text.parse("<white>$value").decoration(TextDecoration.ITALIC, false)
        ))
        item.itemMeta = meta
        return item
    }

    private fun createRitualItem(ritualId: String, catalystMaterial: Material): ItemStack {
        val item = ItemStack(catalystMaterial)
        val meta = item.itemMeta
        meta.displayName(Text.parse("<light_purple>Required Catalyst").decoration(TextDecoration.ITALIC, false))
        meta.lore(listOf(
            Text.parse("<gray>Ritual: <white>$ritualId"),
            Text.parse(""),
            Text.parse("<yellow>Right-click a shrine with this"),
            Text.parse("<yellow>item to begin the ritual.")
        ).map { it.decoration(TextDecoration.ITALIC, false) })
        item.itemMeta = meta
        return item
    }

    private fun createShrineItem(shrineId: String, coreBlock: Material): ItemStack {
        val item = ItemStack(coreBlock)
        val meta = item.itemMeta
        meta.displayName(Text.parse("<aqua>Shrine Structure").decoration(TextDecoration.ITALIC, false))
        meta.lore(listOf(
            Text.parse("<gray>Type: <white>$shrineId"),
            Text.parse(""),
            Text.parse("<yellow>Build this structure and"),
            Text.parse("<yellow>use the catalyst on the core.")
        ).map { it.decoration(TextDecoration.ITALIC, false) })
        item.itemMeta = meta
        return item
    }

    private fun createChannelingItem(ticks: Int): ItemStack {
        val item = ItemStack(Material.CLOCK)
        val meta = item.itemMeta
        val seconds = ticks / 20.0
        meta.displayName(Text.parse("<green>Channeling Time").decoration(TextDecoration.ITALIC, false))
        meta.lore(listOf(
            Text.parse("<white>${"%.1f".format(seconds)} seconds"),
            Text.parse(""),
            Text.parse("<gray>Stand still while channeling."),
            Text.parse("<gray>Moving will cancel the ritual.")
        ).map { it.decoration(TextDecoration.ITALIC, false) })
        item.itemMeta = meta
        return item
    }

    private fun createNoRitualItem(): ItemStack {
        val item = ItemStack(Material.BARRIER)
        val meta = item.itemMeta
        meta.displayName(Text.parse("<red>No Ritual Configured").decoration(TextDecoration.ITALIC, false))
        meta.lore(listOf(
            Text.parse("<gray>This boss cannot be summoned"),
            Text.parse("<gray>through a ritual yet.")
        ).map { it.decoration(TextDecoration.ITALIC, false) })
        item.itemMeta = meta
        return item
    }

    private fun createBackButton(): ItemStack {
        val item = ItemStack(Material.ARROW)
        val meta = item.itemMeta
        meta.displayName(Text.parse("<yellow>← Back to Boss List").decoration(TextDecoration.ITALIC, false))
        item.itemMeta = meta
        return item
    }
}
