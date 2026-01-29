package com.skittlemc.ritualforge.gui

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class GuiHolder(val guiType: GuiType, val data: Map<String, Any> = emptyMap()) : InventoryHolder {

    private var inventory: Inventory? = null

    fun setInventory(inv: Inventory) {
        this.inventory = inv
    }

    override fun getInventory(): Inventory = inventory ?: throw IllegalStateException("Inventory not set")
}

enum class GuiType {
    BOSS_LIST,
    BOSS_INFO
}
