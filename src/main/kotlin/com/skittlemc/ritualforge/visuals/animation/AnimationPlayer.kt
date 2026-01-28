package com.skittlemc.ritualforge.visuals.animation

import com.skittlemc.ritualforge.content.model.AnimationDefinition
import com.skittlemc.ritualforge.content.model.Keyframe
import com.skittlemc.ritualforge.visuals.math.Transform
import org.joml.Vector3f

class AnimationPlayer(private val animation: AnimationDefinition) {

    private var currentTick: Int = 0
    val isFinished: Boolean get() = !animation.looping && currentTick >= animation.lengthTicks

    fun tick() {
        currentTick++
        if (animation.looping && currentTick >= animation.lengthTicks) {
            currentTick = 0
        }
    }

    fun getTransform(boneName: String): Transform {
        val keyframes = animation.keyframes.filter { it.boneName == boneName }
        if (keyframes.isEmpty()) return Transform()

        val prev = keyframes.lastOrNull { it.tick <= currentTick } ?: keyframes.first()
        val next = keyframes.firstOrNull { it.tick > currentTick } ?: keyframes.last()

        if (prev.tick == next.tick) {
            return Transform(prev.position, prev.rotation, prev.scale)
        }

        val t = (currentTick - prev.tick).toFloat() / (next.tick - prev.tick).toFloat()
        return Transform(
            translation = lerp(prev.position, next.position, t),
            rotation = lerp(prev.rotation, next.rotation, t),
            scale = lerp(prev.scale, next.scale, t)
        )
    }

    private fun lerp(a: Vector3f, b: Vector3f, t: Float): Vector3f {
        return Vector3f(
            a.x + (b.x - a.x) * t,
            a.y + (b.y - a.y) * t,
            a.z + (b.z - a.z) * t
        )
    }

    fun reset() {
        currentTick = 0
    }
}
