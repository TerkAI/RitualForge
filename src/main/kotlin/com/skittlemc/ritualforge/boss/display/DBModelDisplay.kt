package com.skittlemc.ritualforge.boss.display

import com.destroystokyo.paper.profile.ProfileProperty
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.util.Transformation
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.UUID

/**
 * Spawns and manages a display entity model created from tools like DisplayBuilder.
 * Uses a block_display as root with item_display passengers for each model part.
 */
class DBModelDisplay(
    private val model: DisplayModel
) {
    private var rootDisplay: BlockDisplay? = null
    private val partDisplays = mutableListOf<ItemDisplay>()

    fun spawn(location: Location) {
        val world = location.world ?: return

        // Spawn root block_display (invisible air block as anchor)
        rootDisplay = world.spawn(location.clone().add(-0.5, -0.5, -0.5), BlockDisplay::class.java) { display ->
            display.block = Material.AIR.createBlockData()
            display.isPersistent = false
        }

        // Spawn each model part as item_display
        for (part in model.parts) {
            val itemDisplay = world.spawn(location, ItemDisplay::class.java) { display ->
                display.setItemStack(createSkullWithTexture(part.textureValue))
                display.itemDisplayTransform = ItemDisplay.ItemDisplayTransform.NONE
                display.transformation = matrixToTransformation(part.transformation)
                display.isPersistent = false
            }
            partDisplays.add(itemDisplay)

            // Add as passenger to root
            rootDisplay?.addPassenger(itemDisplay)
        }
    }

    fun teleportTo(location: Location) {
        rootDisplay?.teleport(location.clone().add(-0.5, -0.5, -0.5))
    }

    fun remove() {
        partDisplays.forEach { it.remove() }
        partDisplays.clear()
        rootDisplay?.remove()
        rootDisplay = null
    }

    fun isSpawned(): Boolean = rootDisplay != null && !rootDisplay!!.isDead

    private fun createSkullWithTexture(textureValue: String): ItemStack {
        val skull = ItemStack(Material.PLAYER_HEAD)
        val meta = skull.itemMeta as? SkullMeta ?: return skull

        val profile = Bukkit.createProfile(UUID.randomUUID())
        profile.setProperty(ProfileProperty("textures", textureValue))
        meta.playerProfile = profile
        skull.itemMeta = meta

        return skull
    }

    private fun matrixToTransformation(matrix: Matrix4f): Transformation {
        // Extract translation from matrix
        val translation = Vector3f(matrix.m30(), matrix.m31(), matrix.m32())

        // Extract scale and rotation using matrix decomposition
        val scaleX = Vector3f(matrix.m00(), matrix.m10(), matrix.m20()).length()
        val scaleY = Vector3f(matrix.m01(), matrix.m11(), matrix.m21()).length()
        val scaleZ = Vector3f(matrix.m02(), matrix.m12(), matrix.m22()).length()
        val scale = Vector3f(scaleX, scaleY, scaleZ)

        // Create rotation matrix by normalizing the scale
        val rotMatrix = Matrix4f(matrix)
        if (scaleX != 0f) {
            rotMatrix.m00(matrix.m00() / scaleX)
            rotMatrix.m10(matrix.m10() / scaleX)
            rotMatrix.m20(matrix.m20() / scaleX)
        }
        if (scaleY != 0f) {
            rotMatrix.m01(matrix.m01() / scaleY)
            rotMatrix.m11(matrix.m11() / scaleY)
            rotMatrix.m21(matrix.m21() / scaleY)
        }
        if (scaleZ != 0f) {
            rotMatrix.m02(matrix.m02() / scaleZ)
            rotMatrix.m12(matrix.m12() / scaleZ)
            rotMatrix.m22(matrix.m22() / scaleZ)
        }

        // Extract rotation as quaternion
        val rotation = Quaternionf().setFromNormalized(rotMatrix)

        return Transformation(
            translation,
            rotation,
            scale,
            Quaternionf() // No right rotation
        )
    }

    companion object {
        /**
         * Parse a transformation array from the summon command format.
         * Format: [m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33]
         */
        fun parseTransformation(values: FloatArray): Matrix4f {
            require(values.size == 16) { "Transformation must have 16 values" }
            return Matrix4f(
                values[0], values[4], values[8], values[12],
                values[1], values[5], values[9], values[13],
                values[2], values[6], values[10], values[14],
                values[3], values[7], values[11], values[15]
            )
        }
    }
}
