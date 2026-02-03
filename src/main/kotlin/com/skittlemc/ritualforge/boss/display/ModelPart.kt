package com.skittlemc.ritualforge.boss.display

import org.joml.Matrix4f

/**
 * Represents a single part of a display entity model.
 * Each part is an item_display with a player head texture and transformation.
 */
data class ModelPart(
    val textureValue: String,
    val transformation: Matrix4f
)

/**
 * Represents a complete display entity model with multiple parts.
 */
data class DisplayModel(
    val id: String,
    val parts: List<ModelPart>
)
