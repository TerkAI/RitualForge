package com.skittlemc.ritualforge.api.events

import com.skittlemc.ritualforge.boss.BossInstance
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class BossSpawnEvent(val boss: BossInstance) : Event() {

    override fun getHandlers(): HandlerList = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
