package com.skittlemc.ritualforge.util

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit

object Tasks {

    // ── Global Region Scheduler ──

    fun global(plugin: Plugin, block: (ScheduledTask) -> Unit) {
        Bukkit.getGlobalRegionScheduler().run(plugin, block)
    }

    fun globalDelayed(plugin: Plugin, delayTicks: Long, block: (ScheduledTask) -> Unit) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, block, delayTicks)
    }

    fun globalRepeat(
        plugin: Plugin,
        initialDelayTicks: Long,
        periodTicks: Long,
        block: (ScheduledTask) -> Unit
    ): ScheduledTask {
        return Bukkit.getGlobalRegionScheduler().runAtFixedRate(
            plugin, block, initialDelayTicks.coerceAtLeast(1), periodTicks
        )
    }

    // ── Region Scheduler (location-bound) ──

    fun region(plugin: Plugin, location: Location, block: (ScheduledTask) -> Unit) {
        Bukkit.getRegionScheduler().run(plugin, location, block)
    }

    fun regionDelayed(plugin: Plugin, location: Location, delayTicks: Long, block: (ScheduledTask) -> Unit) {
        Bukkit.getRegionScheduler().runDelayed(plugin, location, block, delayTicks)
    }

    fun regionRepeat(
        plugin: Plugin,
        location: Location,
        initialDelayTicks: Long,
        periodTicks: Long,
        block: (ScheduledTask) -> Unit
    ): ScheduledTask {
        return Bukkit.getRegionScheduler().runAtFixedRate(
            plugin, location, block, initialDelayTicks.coerceAtLeast(1), periodTicks
        )
    }

    // ── Entity Scheduler ──

    fun entity(plugin: Plugin, entity: Entity, block: (ScheduledTask) -> Unit): Boolean {
        return entity.scheduler.run(plugin, block, null) != null
    }

    fun entityDelayed(
        plugin: Plugin,
        entity: Entity,
        delayTicks: Long,
        block: (ScheduledTask) -> Unit
    ): Boolean {
        return entity.scheduler.runDelayed(plugin, block, null, delayTicks) != null
    }

    fun entityRepeat(
        plugin: Plugin,
        entity: Entity,
        initialDelayTicks: Long,
        periodTicks: Long,
        block: (ScheduledTask) -> Unit
    ): ScheduledTask? {
        return entity.scheduler.runAtFixedRate(
            plugin, block, null, initialDelayTicks.coerceAtLeast(1), periodTicks
        )
    }

    // ── Async Scheduler ──

    fun async(plugin: Plugin, block: (ScheduledTask) -> Unit) {
        Bukkit.getAsyncScheduler().runNow(plugin, block)
    }

    fun asyncDelayed(plugin: Plugin, delayMs: Long, block: (ScheduledTask) -> Unit) {
        Bukkit.getAsyncScheduler().runDelayed(plugin, block, delayMs, TimeUnit.MILLISECONDS)
    }

    fun asyncRepeat(
        plugin: Plugin,
        initialDelayMs: Long,
        periodMs: Long,
        block: (ScheduledTask) -> Unit
    ): ScheduledTask {
        return Bukkit.getAsyncScheduler().runAtFixedRate(
            plugin, block, initialDelayMs.coerceAtLeast(1), periodMs, TimeUnit.MILLISECONDS
        )
    }
}
