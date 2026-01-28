package com.skittlemc.ritualforge.content.model

import org.joml.Vector3f

data class AnimationDefinition(
    val id: String,
    val lengthTicks: Int,
    val looping: Boolean = false,
    val keyframes: List<Keyframe>
)

data class Keyframe(
    val tick: Int,
    val boneName: String,
    val position: Vector3f = Vector3f(),
    val rotation: Vector3f = Vector3f(),
    val scale: Vector3f = Vector3f(1f, 1f, 1f)
)
