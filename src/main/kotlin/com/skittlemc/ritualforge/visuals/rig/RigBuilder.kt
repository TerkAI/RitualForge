package com.skittlemc.ritualforge.visuals.rig

import com.skittlemc.ritualforge.content.model.ModelDefinition
import com.skittlemc.ritualforge.visuals.math.Transform
import org.joml.Vector3f

class RigBuilder {

    fun build(model: ModelDefinition): Rig {
        val bones = model.bones.associate { entry ->
            entry.name to Bone(
                name = entry.name,
                parent = entry.parent,
                localTransform = Transform(
                    translation = entry.offset,
                    rotation = Vector3f(),
                    scale = Vector3f(1f, 1f, 1f)
                ),
                customModelData = entry.customModelData
            )
        }
        return Rig(bones)
    }
}
