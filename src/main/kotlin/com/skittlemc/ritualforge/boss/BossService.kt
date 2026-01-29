package com.skittlemc.ritualforge.boss

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.api.events.BossDeathEvent
import com.skittlemc.ritualforge.api.events.BossSpawnEvent
import com.skittlemc.ritualforge.content.boss.BossDefinition
import com.skittlemc.ritualforge.util.Keys
import com.skittlemc.ritualforge.util.Tasks
import com.skittlemc.ritualforge.util.Text
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
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

        // Spawn the entity on the correct region thread
        Tasks.region(plugin, location) {
            val entity = spawnEntity(definition, location, instance.uuid)
            instance.entity = entity
        }

        instances[instance.uuid] = instance
        Bukkit.getPluginManager().callEvent(BossSpawnEvent(instance))
        plugin.logger.info("Spawned boss '${definition.id}' at ${location.toVector()}")
        return instance
    }

    private fun spawnEntity(definition: BossDefinition, location: Location, bossId: UUID): LivingEntity? {
        val world = location.world ?: return null

        val entity = world.spawnEntity(location, definition.entityType) as? LivingEntity ?: return null

        // Set custom name
        entity.customName(Text.parse("<gradient:red:gold><bold>${definition.displayName}"))
        entity.isCustomNameVisible = true

        // Set health
        entity.getAttribute(Attribute.MAX_HEALTH)?.baseValue = definition.maxHealth
        entity.health = definition.maxHealth

        // Set glowing
        if (definition.glowing) {
            entity.isGlowing = true
        }

        // Prevent despawn
        entity.isPersistent = true
        if (entity is Mob) {
            entity.removeWhenFarAway = false
        }

        // Mark as boss with PDC
        entity.persistentDataContainer.set(Keys.BOSS_ID, PersistentDataType.STRING, bossId.toString())

        // Set equipment
        val equipment = entity.equipment
        if (equipment != null) {
            definition.equipment.mainHand?.let { equipment.setItemInMainHand(ItemStack(it)) }
            definition.equipment.offHand?.let { equipment.setItemInOffHand(ItemStack(it)) }
            definition.equipment.helmet?.let { equipment.helmet = ItemStack(it) }
            definition.equipment.chestplate?.let { equipment.chestplate = ItemStack(it) }
            definition.equipment.leggings?.let { equipment.leggings = ItemStack(it) }
            definition.equipment.boots?.let { equipment.boots = ItemStack(it) }

            // Don't drop equipment
            equipment.helmetDropChance = 0f
            equipment.chestplateDropChance = 0f
            equipment.leggingsDropChance = 0f
            equipment.bootsDropChance = 0f
            equipment.itemInMainHandDropChance = 0f
            equipment.itemInOffHandDropChance = 0f
        }

        return entity
    }

    fun despawn(instanceId: UUID) {
        val instance = instances.remove(instanceId) ?: return
        instance.kill()
        plugin.logger.info("Despawned boss '${instance.definition.id}'")
    }

    fun getInstance(id: UUID): BossInstance? = instances[id]

    fun getInstanceByEntity(entity: LivingEntity): BossInstance? {
        val bossIdStr = entity.persistentDataContainer.get(Keys.BOSS_ID, PersistentDataType.STRING)
            ?: return null
        val bossId = runCatching { UUID.fromString(bossIdStr) }.getOrNull() ?: return null
        return instances[bossId]
    }

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
