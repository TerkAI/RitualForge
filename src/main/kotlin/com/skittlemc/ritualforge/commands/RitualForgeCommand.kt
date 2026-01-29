package com.skittlemc.ritualforge.commands

import com.skittlemc.ritualforge.RitualForgePlugin
import com.skittlemc.ritualforge.util.Text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class RitualForgeCommand(private val plugin: RitualForgePlugin) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Text.parse("<red>This command can only be used by players."))
            return true
        }

        when (args.getOrNull(0)?.lowercase()) {
            "reload" -> {
                if (!sender.hasPermission("ritualforge.reload")) {
                    sender.sendMessage(Text.parse("<red>You don't have permission to do that."))
                    return true
                }
                plugin.contentManager.loadAll()
                sender.sendMessage(Text.parse("<green>RitualForge content reloaded."))
            }
            "list" -> {
                plugin.guiManager.openBossList(sender)
            }
            "info" -> {
                val bossId = args.getOrNull(1)
                if (bossId == null) {
                    sender.sendMessage(Text.parse("<red>Usage: /$label info <boss_id>"))
                    return true
                }
                plugin.guiManager.openBossInfo(sender, bossId)
            }
            else -> {
                plugin.guiManager.openBossList(sender)
            }
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        return when (args.size) {
            1 -> listOf("list", "info", "reload").filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> if (args[0].equals("info", ignoreCase = true)) {
                plugin.contentManager.getAllBossDefinitions()
                    .map { it.id }
                    .filter { it.startsWith(args[1], ignoreCase = true) }
            } else emptyList()
            else -> emptyList()
        }
    }
}
