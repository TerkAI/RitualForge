package com.skittlemc.ritualforge.boss.display

import com.skittlemc.ritualforge.util.Text
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f
import java.net.URI
import java.util.UUID

class BlockBossDisplay(
    private val displayName: String,
    private val skinTexture: String?,
    private val bodyColor: Material = Material.CYAN_TERRACOTTA,
    private val limbColor: Material = Material.BLUE_TERRACOTTA
) {
    // All display entities that make up this boss
    private val displays = mutableListOf<Display>()
    private var headDisplay: ItemDisplay? = null
    private var bodyDisplay: BlockDisplay? = null
    private var leftArmDisplay: BlockDisplay? = null
    private var rightArmDisplay: BlockDisplay? = null
    private var leftLegDisplay: BlockDisplay? = null
    private var rightLegDisplay: BlockDisplay? = null

    // Animation state
    private var animationTick = 0

    fun spawn(location: Location) {
        val world = location.world ?: return

        // Scale factor (Blockbench uses 16 units = 1 block)
        val scale = 1f / 16f

        // Head (ItemDisplay with player head) - positioned at top
        val head = world.spawnEntity(location.clone().add(0.0, 1.5, 0.0), EntityType.ITEM_DISPLAY) as ItemDisplay
        head.setItemStack(createSkullWithSkin(skinTexture ?: ""))
        head.transformation = Transformation(
            Vector3f(-0.25f, 0f, -0.25f),  // translation
            AxisAngle4f(0f, 0f, 0f, 1f),   // left rotation
            Vector3f(0.5f, 0.5f, 0.5f),    // scale
            AxisAngle4f(0f, 0f, 0f, 1f)    // right rotation
        )
        head.brightness = Display.Brightness(15, 15)
        headDisplay = head
        displays.add(head)

        // Body (BlockDisplay)
        val body = world.spawnEntity(location.clone().add(0.0, 0.75, 0.0), EntityType.BLOCK_DISPLAY) as BlockDisplay
        body.block = bodyColor.createBlockData()
        body.transformation = Transformation(
            Vector3f(-0.25f, 0f, -0.125f),
            AxisAngle4f(0f, 0f, 0f, 1f),
            Vector3f(0.5f, 0.75f, 0.25f),
            AxisAngle4f(0f, 0f, 0f, 1f)
        )
        bodyDisplay = body
        displays.add(body)

        // Right Arm
        val rightArm = world.spawnEntity(location.clone().add(0.35, 0.75, 0.0), EntityType.BLOCK_DISPLAY) as BlockDisplay
        rightArm.block = limbColor.createBlockData()
        rightArm.transformation = Transformation(
            Vector3f(-0.0625f, 0f, -0.0625f),
            AxisAngle4f(0f, 0f, 0f, 1f),
            Vector3f(0.25f, 0.75f, 0.25f),
            AxisAngle4f(0f, 0f, 0f, 1f)
        )
        rightArmDisplay = rightArm
        displays.add(rightArm)

        // Left Arm
        val leftArm = world.spawnEntity(location.clone().add(-0.35, 0.75, 0.0), EntityType.BLOCK_DISPLAY) as BlockDisplay
        leftArm.block = limbColor.createBlockData()
        leftArm.transformation = Transformation(
            Vector3f(-0.0625f, 0f, -0.0625f),
            AxisAngle4f(0f, 0f, 0f, 1f),
            Vector3f(0.25f, 0.75f, 0.25f),
            AxisAngle4f(0f, 0f, 0f, 1f)
        )
        leftArmDisplay = leftArm
        displays.add(leftArm)

        // Right Leg
        val rightLeg = world.spawnEntity(location.clone().add(0.125, 0.0, 0.0), EntityType.BLOCK_DISPLAY) as BlockDisplay
        rightLeg.block = limbColor.createBlockData()
        rightLeg.transformation = Transformation(
            Vector3f(-0.125f, 0f, -0.0625f),
            AxisAngle4f(0f, 0f, 0f, 1f),
            Vector3f(0.25f, 0.75f, 0.25f),
            AxisAngle4f(0f, 0f, 0f, 1f)
        )
        rightLegDisplay = rightLeg
        displays.add(rightLeg)

        // Left Leg
        val leftLeg = world.spawnEntity(location.clone().add(-0.125, 0.0, 0.0), EntityType.BLOCK_DISPLAY) as BlockDisplay
        leftLeg.block = limbColor.createBlockData()
        leftLeg.transformation = Transformation(
            Vector3f(-0.125f, 0f, -0.0625f),
            AxisAngle4f(0f, 0f, 0f, 1f),
            Vector3f(0.25f, 0.75f, 0.25f),
            AxisAngle4f(0f, 0f, 0f, 1f)
        )
        leftLegDisplay = leftLeg
        displays.add(leftLeg)
    }

    fun teleportTo(location: Location) {
        headDisplay?.teleport(location.clone().add(0.0, 1.5, 0.0))
        bodyDisplay?.teleport(location.clone().add(0.0, 0.75, 0.0))
        rightArmDisplay?.teleport(location.clone().add(0.35, 0.75, 0.0))
        leftArmDisplay?.teleport(location.clone().add(-0.35, 0.75, 0.0))
        rightLegDisplay?.teleport(location.clone().add(0.125, 0.0, 0.0))
        leftLegDisplay?.teleport(location.clone().add(-0.125, 0.0, 0.0))
    }

    fun animateIdle() {
        animationTick++

        // Simple bobbing animation for idle
        val bob = Math.sin(animationTick * 0.1) * 0.05

        // Arm swing
        val armSwing = Math.sin(animationTick * 0.15).toFloat() * 0.2f

        rightArmDisplay?.let { arm ->
            val t = arm.transformation
            arm.transformation = Transformation(
                t.translation,
                AxisAngle4f(armSwing, 1f, 0f, 0f),
                t.scale,
                t.rightRotation
            )
        }

        leftArmDisplay?.let { arm ->
            val t = arm.transformation
            arm.transformation = Transformation(
                t.translation,
                AxisAngle4f(-armSwing, 1f, 0f, 0f),
                t.scale,
                t.rightRotation
            )
        }
    }

    fun animateWalk() {
        animationTick++

        val legSwing = Math.sin(animationTick * 0.3).toFloat() * 0.5f
        val armSwing = Math.sin(animationTick * 0.3).toFloat() * 0.4f

        rightLegDisplay?.let { leg ->
            val t = leg.transformation
            leg.transformation = Transformation(
                t.translation,
                AxisAngle4f(legSwing, 1f, 0f, 0f),
                t.scale,
                t.rightRotation
            )
        }

        leftLegDisplay?.let { leg ->
            val t = leg.transformation
            leg.transformation = Transformation(
                t.translation,
                AxisAngle4f(-legSwing, 1f, 0f, 0f),
                t.scale,
                t.rightRotation
            )
        }

        rightArmDisplay?.let { arm ->
            val t = arm.transformation
            arm.transformation = Transformation(
                t.translation,
                AxisAngle4f(-armSwing, 1f, 0f, 0f),
                t.scale,
                t.rightRotation
            )
        }

        leftArmDisplay?.let { arm ->
            val t = arm.transformation
            arm.transformation = Transformation(
                t.translation,
                AxisAngle4f(armSwing, 1f, 0f, 0f),
                t.scale,
                t.rightRotation
            )
        }
    }

    fun remove() {
        displays.forEach { it.remove() }
        displays.clear()
        headDisplay = null
        bodyDisplay = null
        leftArmDisplay = null
        rightArmDisplay = null
        leftLegDisplay = null
        rightLegDisplay = null
    }

    fun getDisplays(): List<Display> = displays.toList()

    private fun createSkullWithSkin(skinTexture: String): ItemStack {
        val skull = ItemStack(Material.PLAYER_HEAD)
        if (skinTexture.isBlank()) return skull

        val meta = skull.itemMeta as? SkullMeta ?: return skull

        val profile = Bukkit.createPlayerProfile(UUID.randomUUID())
        val textures = profile.textures

        val textureUrl = if (skinTexture.startsWith("http")) {
            skinTexture
        } else {
            "https://textures.minecraft.net/texture/$skinTexture"
        }

        try {
            textures.skin = URI(textureUrl).toURL()
            profile.setTextures(textures)
            meta.ownerProfile = profile
        } catch (e: Exception) {
            // Failed to set skin
        }

        skull.itemMeta = meta
        return skull
    }
}
