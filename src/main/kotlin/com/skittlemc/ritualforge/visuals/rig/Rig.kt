package com.skittlemc.ritualforge.visuals.rig

import com.skittlemc.ritualforge.visuals.math.Transform

class Rig(val bones: Map<String, Bone>) {

    fun getBone(name: String): Bone? = bones[name]

    fun rootBones(): List<Bone> = bones.values.filter { it.parent == null }
}

data class Bone(
    val name: String,
    val parent: String? = null,
    val localTransform: Transform = Transform(),
    var worldTransform: Transform = Transform(),
    val customModelData: Int? = null
)
