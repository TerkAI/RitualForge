package com.skittlemc.ritualforge.boss.display

import com.skittlemc.ritualforge.util.Text
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.profile.PlayerProfile
import java.net.URI
import java.util.UUID

class SkinBossDisplay(
    private val skinUrl: String,
    private val displayName: String
) {
    private var armorStand: ArmorStand? = null

    fun spawn(location: Location): ArmorStand? {
        val world = location.world ?: return null

        val stand = world.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand

        // Configure armor stand
        stand.isVisible = false
        stand.isInvulnerable = true
        stand.setGravity(false)
        stand.customName(Text.parse("<gradient:red:gold><bold>$displayName"))
        stand.isCustomNameVisible = true

        // Set the player head with custom skin
        val skull = createSkullWithSkin(skinUrl)
        stand.equipment.helmet = skull

        // Optional: Add body armor for a more complete look
        stand.equipment.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
        stand.equipment.leggings = ItemStack(Material.NETHERITE_LEGGINGS)
        stand.equipment.boots = ItemStack(Material.NETHERITE_BOOTS)

        armorStand = stand
        return stand
    }

    fun remove() {
        armorStand?.remove()
        armorStand = null
    }

    fun teleport(location: Location) {
        armorStand?.teleport(location)
    }

    fun getEntity(): ArmorStand? = armorStand

    companion object {
        fun createSkullWithSkin(skinUrl: String): ItemStack {
            val skull = ItemStack(Material.PLAYER_HEAD)
            val meta = skull.itemMeta as? SkullMeta ?: return skull

            // Create a profile with the skin texture
            val profile: PlayerProfile = Bukkit.createPlayerProfile(UUID.randomUUID())
            val textures = profile.textures

            // Set the skin URL
            // Format: https://textures.minecraft.net/texture/TEXTURE_HASH
            val textureUrl = if (skinUrl.startsWith("http")) {
                skinUrl
            } else {
                "https://textures.minecraft.net/texture/$skinUrl"
            }

            try {
                textures.skin = URI(textureUrl).toURL()
                profile.setTextures(textures)
                meta.ownerProfile = profile
            } catch (e: Exception) {
                // Fallback: just use the hash as a player name lookup
            }

            skull.itemMeta = meta
            return skull
        }
    }
}
