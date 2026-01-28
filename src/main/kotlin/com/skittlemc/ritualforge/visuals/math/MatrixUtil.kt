package com.skittlemc.ritualforge.visuals.math

import org.joml.Matrix4f
import org.joml.Vector3f

object MatrixUtil {

    fun compose(translation: Vector3f, rotation: Vector3f, scale: Vector3f): Matrix4f {
        return Matrix4f()
            .translate(translation)
            .rotateXYZ(
                Math.toRadians(rotation.x.toDouble()).toFloat(),
                Math.toRadians(rotation.y.toDouble()).toFloat(),
                Math.toRadians(rotation.z.toDouble()).toFloat()
            )
            .scale(scale)
    }

    fun decompose(matrix: Matrix4f): Transform {
        val translation = Vector3f()
        matrix.getTranslation(translation)

        val scale = Vector3f()
        matrix.getScale(scale)

        val eulerAngles = Vector3f()
        matrix.getEulerAnglesXYZ(eulerAngles)

        return Transform(
            translation = translation,
            rotation = Vector3f(
                Math.toDegrees(eulerAngles.x.toDouble()).toFloat(),
                Math.toDegrees(eulerAngles.y.toDouble()).toFloat(),
                Math.toDegrees(eulerAngles.z.toDouble()).toFloat()
            ),
            scale = scale
        )
    }
}
