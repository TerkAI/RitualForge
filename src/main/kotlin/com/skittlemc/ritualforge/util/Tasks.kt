package com.skittlemc.ritualforge.util

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

object Tasks {

    fun sync(plugin: Plugin, block: () -> Unit): Int {
        return Bukkit.getScheduler().runTask(plugin, block).taskId
    }

    fun delayed(plugin: Plugin, delayTicks: Long, block: () -> Unit): Int {
        return Bukkit.getScheduler().runTaskLater(plugin, block, delayTicks).taskId
    }

    fun repeat(plugin: Plugin, delayTicks: Long, periodTicks: Long, block: (taskId: Int) -> Unit): Int {
        var id = -1
        id = Bukkit.getScheduler().runTaskTimer(plugin, Runnable { block(id) }, delayTicks, periodTicks).taskId
        return id
    }

    fun repeatAsync(plugin: Plugin, delayTicks: Long, periodTicks: Long, block: () -> Unit): Int {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, block, delayTicks, periodTicks).taskId
    }

    fun async(plugin: Plugin, block: () -> Unit): Int {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, block).taskId
    }
}
