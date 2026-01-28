package com.skittlemc.ritualforge.visuals.animation

import com.skittlemc.ritualforge.visuals.math.Transform
import org.joml.Vector3f

class AnimationBlender {

    fun blend(a: Transform, b: Transform, weight: Float): Transform {
        val w = weight.coerceIn(0f, 1f)
        return Transform(
            translation = lerp(a.translation, b.translation, w),
            rotation = lerp(a.rotation, b.rotation, w),
            scale = lerp(a.scale, b.scale, w)
        )
    }

    private fun lerp(a: Vector3f, b: Vector3f, t: Float): Vector3f {
        return Vector3f(
            a.x + (b.x - a.x) * t,
            a.y + (b.y - a.y) * t,
            a.z + (b.z - a.z) * t
        )
    }
}
