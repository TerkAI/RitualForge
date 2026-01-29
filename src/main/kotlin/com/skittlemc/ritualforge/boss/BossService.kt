package com.skittlemc.ritualforge.boss

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.api.events.BossDeathEvent
import com.skittlemc.ritualforge.api.events.BossSpawnEvent
import com.skittlemc.ritualforge.content.boss.BossDefinition
import com.skittlemc.ritualforge.util.Tasks
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class BossService(private val plugin: RitualForgePlugin) {

    private val instances = ConcurrentHashMap<UUID, BossInstance>()
    private var tickTask: ScheduledTask? = null

    fun start() {
        tickTask = Tasks.globalRepeat(plugin, 1L, 1L) { tickAll() }
    }

    fun spawn(definition: BossDefinition, location: Location): BossInstance {
        val arena = Arena(location, definition.arenaRadius)
        val instance = BossInstance(
            definition = definition,
            spawnLocation = location,
            arena = arena
        )
        instances[instance.uuid] = instance
        Bukkit.getPluginManager().callEvent(BossSpawnEvent(instance))
        plugin.logger.info("Spawned boss '${definition.id}' at ${location.toVector()}")
        return instance
    }

    fun despawn(instanceId: UUID) {
        val instance = instances.remove(instanceId) ?: return
        instance.kill()
        plugin.logger.info("Despawned boss '${instance.definition.id}'")
    }

    fun getInstance(id: UUID): BossInstance? = instances[id]

    fun activeBosses(): Collection<BossInstance> = instances.values

    fun shutdown() {
        tickTask?.cancel()
        instances.keys.toList().forEach { despawn(it) }
    }

    private fun tickAll() {
        val dead = mutableListOf<UUID>()
        for ((id, instance) in instances) {
            if (!instance.alive) {
                dead += id
                continue
            }
            instance.tick()
            if (instance.health.isDead) {
                instance.kill()
                Bukkit.getPluginManager().callEvent(BossDeathEvent(instance))
                dead += id
            }
        }
        dead.forEach { instances.remove(it) }
    }
}
