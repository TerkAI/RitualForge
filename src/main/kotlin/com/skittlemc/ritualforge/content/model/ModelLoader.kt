package com.skittlemc.ritualforge.content.model

import com.skittlemc.ritualforge.RitualForgePlugin
import org.bukkit.configuration.file.YamlConfiguration
import org.joml.Vector3f
import java.io.File

class ModelLoader(private val plugin: RitualForgePlugin) {

    private val models = mutableMapOf<String, ModelDefinition>()

    fun load(): Map<String, ModelDefinition> {
        models.clear()
        val folder = File(plugin.dataFolder, "models")
        if (!folder.exists()) {
            folder.mkdirs()
            return models
        }

        folder.listFiles { f -> f.extension == "yml" }?.forEach { file ->
            runCatching { parse(file) }.onSuccess {
                models[it.id] = it
            }.onFailure {
                plugin.logger.warning("Failed to load model from ${file.name}: ${it.message}")
            }
        }

        plugin.logger.info("Loaded ${models.size} models.")
        return models
    }

    fun getModel(id: String): ModelDefinition? = models[id]

    private fun parse(file: File): ModelDefinition {
        val cfg = YamlConfiguration.loadConfiguration(file)
        val id = file.nameWithoutExtension

        val bones = cfg.getMapList("bones").map { map ->
            BoneEntry(
                name = map["name"] as? String ?: "root",
                parent = map["parent"] as? String,
                offset = parseVector(map["offset"]),
                customModelData = (map["custom_model_data"] as? Number)?.toInt()
            )
        }

        val animations = cfg.getStringList("animations")

        return ModelDefinition(id = id, bones = bones, animations = animations)
    }

    private fun parseVector(raw: Any?): Vector3f {
        if (raw is List<*> && raw.size >= 3) {
            return Vector3f(
                (raw[0] as? Number)?.toFloat() ?: 0f,
                (raw[1] as? Number)?.toFloat() ?: 0f,
                (raw[2] as? Number)?.toFloat() ?: 0f
            )
        }
        return Vector3f()
    }
}
