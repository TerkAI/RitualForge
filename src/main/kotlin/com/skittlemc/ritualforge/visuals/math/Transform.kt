package com.skittlemc.ritualforge.visuals.math

import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Quaternionf
import org.joml.Vector3f

data class Transform(
    val translation: Vector3f = Vector3f(),
    val rotation: Vector3f = Vector3f(),
    val scale: Vector3f = Vector3f(1f, 1f, 1f)
) {
    fun toBukkitTransformation(): Transformation {
        val leftRotation = Quaternionf().rotateXYZ(
            Math.toRadians(rotation.x.toDouble()).toFloat(),
            Math.toRadians(rotation.y.toDouble()).toFloat(),
            Math.toRadians(rotation.z.toDouble()).toFloat()
        )
        return Transformation(
            translation,
            leftRotation,
            scale,
            Quaternionf()
        )
    }
}
