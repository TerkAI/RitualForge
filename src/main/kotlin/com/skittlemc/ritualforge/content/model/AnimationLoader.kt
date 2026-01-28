package com.skittlemc.ritualforge.content.model

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.configuration.file.YamlConfiguration
import org.joml.Vector3f
import java.io.File

class AnimationLoader(private val plugin: RitualForgePlugin) {

    private val animations = mutableMapOf<String, AnimationDefinition>()

    fun load(): Map<String, AnimationDefinition> {
        animations.clear()
        val folder = File(plugin.dataFolder, "animations")
        if (!folder.exists()) {
            folder.mkdirs()
            return animations
        }

        folder.listFiles { f -> f.extension == "yml" }?.forEach { file ->
            runCatching { parse(file) }.onSuccess {
                animations[it.id] = it
            }.onFailure {
                plugin.logger.warning("Failed to load animation from ${file.name}: ${it.message}")
            }
        }

        return animations
    }

    fun getAnimation(id: String): AnimationDefinition? = animations[id]

    private fun parse(file: File): AnimationDefinition {
        val cfg = YamlConfiguration.loadConfiguration(file)
        val id = file.nameWithoutExtension

        val keyframes = cfg.getMapList("keyframes").map { map ->
            Keyframe(
                tick = (map["tick"] as? Number)?.toInt() ?: 0,
                boneName = map["bone"] as? String ?: "root",
                position = parseVector(map["position"]),
                rotation = parseVector(map["rotation"]),
                scale = parseVector(map["scale"], default = Vector3f(1f, 1f, 1f))
            )
        }

        return AnimationDefinition(
            id = id,
            lengthTicks = cfg.getInt("length_ticks", 20),
            looping = cfg.getBoolean("looping", false),
            keyframes = keyframes
        )
    }

    private fun parseVector(raw: Any?, default: Vector3f = Vector3f()): Vector3f {
        if (raw is List<*> && raw.size >= 3) {
            return Vector3f(
                (raw[0] as? Number)?.toFloat() ?: default.x,
                (raw[1] as? Number)?.toFloat() ?: default.y,
                (raw[2] as? Number)?.toFloat() ?: default.z
            )
        }
        return default
    }
}
