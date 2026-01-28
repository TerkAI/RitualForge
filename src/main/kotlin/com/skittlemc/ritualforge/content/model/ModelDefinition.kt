package com.skittlemc.ritualforge.content.model

import org.joml.Vector3f

data class ModelDefinition(
    val id: String,
    val bones: List<BoneEntry>,
    val animations: List<String> = emptyList()
)

data class BoneEntry(
    val name: String,
    val parent: String? = null,
    val offset: Vector3f = Vector3f(),
    val customModelData: Int? = null
)
