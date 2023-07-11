package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

class GameSystem {
    private var gameTask: BukkitTask? = null
    fun system(plugin: Plugin, player: Player, item_name: String) {
        if (item_name == "${ChatColor.AQUA}ゲームスタート") {
            start(plugin, player)
            return
        }
        if (item_name == "${ChatColor.RED}終了") {
            stop(plugin, player)
            return
        }
    }
    fun start(plugin: Plugin, player: Player) {
        if (Events.DataManager.gameData.status) {
            player.sendMessage("${ChatColor.RED}既にゲームはスタートしています")
            return
        }
        Bukkit.broadcastMessage("${ChatColor.GREEN}攻防戦ゲームスタート！！")
        Events.DataManager.gameData.status = true
        gameTask = Bukkit.getScheduler().runTaskTimer(
            plugin,
            Runnable {
                Events.DataManager.gameData.time += 1
            },
            0L, 20L
        )
    }

    fun stop(plugin: Plugin, player: Player) {
        if (!Events.DataManager.gameData.status) {
            player.sendMessage("${ChatColor.RED}ゲームは開始していません")
            return
        }
        Bukkit.broadcastMessage("${ChatColor.RED}攻防戦ゲーム強制終了！！")
        Events.DataManager.gameData.status = false
        gameTask?.cancel()
        gameTask = null
    }
    fun gameend() {
        Bukkit.broadcastMessage("${ChatColor.RED}攻防戦ゲーム終了！！")
        Events.DataManager.gameData.status = false
        gameTask?.cancel()
        gameTask = null
    }
}
