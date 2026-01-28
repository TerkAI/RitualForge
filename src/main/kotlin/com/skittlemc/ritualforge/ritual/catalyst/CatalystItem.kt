package com.skittlemc.ritualforge.ritual.catalyst

import com.skittlemc.ritualforge.util.Keys
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CatalystItem(
    val id: String,
    val material: Material = Material.NETHER_STAR,
    val displayName: String = "Ritual Catalyst",
    val customModelData: Int? = null
) {
    fun createItemStack(): ItemStack {
        val stack = ItemStack(material)
        val meta = stack.itemMeta ?: return stack

        meta.setDisplayName(displayName)
        meta.persistentDataContainer.set(
            Keys.CATALYST_ID,
            PersistentDataType.STRING,
            id
        )
        customModelData?.let { meta.setCustomModelData(it) }

        stack.itemMeta = meta
        return stack
    }

    companion object {
        fun getCatalystId(item: ItemStack): String? {
            val meta = item.itemMeta ?: return null
            return meta.persistentDataContainer.get(
                Keys.CATALYST_ID,
                PersistentDataType.STRING
            )
        }
    }
}
