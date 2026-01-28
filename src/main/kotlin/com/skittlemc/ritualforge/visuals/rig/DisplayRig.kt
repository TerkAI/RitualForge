package com.skittlemc.ritualforge.visuals.rig

import org.bukkit.Location
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

class DisplayRig(private val rig: Rig) {

    private val displayEntities = mutableMapOf<String, ItemDisplay>()

    fun spawn(origin: Location) {
        val world = origin.world ?: return
        for ((name, bone) in rig.bones) {
            val entity = world.spawn(origin, ItemDisplay::class.java) { display ->
                bone.customModelData?.let { cmd ->
                    val stack = ItemStack(Material.PAPER)
                    val meta = stack.itemMeta
                    meta?.setCustomModelData(cmd)
                    stack.itemMeta = meta
                    display.setItemStack(stack)
                }
            }
            displayEntities[name] = entity
        }
    }

    fun update() {
        for ((name, bone) in rig.bones) {
            val entity = displayEntities[name] ?: continue
            val transform = bone.worldTransform
            entity.transformation = transform.toBukkitTransformation()
        }
    }

    fun destroy() {
        displayEntities.values.forEach { it.remove() }
        displayEntities.clear()
    }
}
