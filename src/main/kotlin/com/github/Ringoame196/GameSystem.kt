package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class GameSystem {
    fun start(plugin: Plugin, player: Player) {
        if (Events.DataManager.gameData.status) {
            player.sendMessage("${ChatColor.RED}既にゲームはスタートしています")
            return
        }
        Bukkit.broadcastMessage("攻防戦ゲームスタート！！")
        Events.DataManager.gameData.status = true
        Bukkit.getScheduler().runTaskTimer(
            plugin,
            Runnable {
                Events.DataManager.gameData.time += 1
            },
            0L, 20L
        )
    }
}
