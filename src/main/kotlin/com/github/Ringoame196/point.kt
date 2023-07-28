package com.github.Ringoame196

import jdk.jfr.Event
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class point {
    fun add(player: Player, addpoint: Int) {
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        var point = Data.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        point += addpoint
        player.sendMessage("${ChatColor.GREEN}+$addpoint (${point}ポイント)")
        Data.DataManager.playerDataMap[player.uniqueId]?.let { playerData ->
            playerData.point = point
        }
    }
    fun ore(e: org.bukkit.event.Event, player: Player, block: Block, team: String, plugin: Plugin) {
        val block_type = block.type
        GameSystem().adventure(e, player)
        var cooltime = Data.DataManager.teamDataMap.getOrPut(team) { Team() }.blockTime
        var point = 0
        when (block_type) {
            Material.COAL_ORE -> { point += 1 }

            Material.IRON_ORE -> { point += 3 }

            Material.GOLD_ORE -> { point += 5 }

            Material.DIAMOND_ORE -> {
                point += 100
                cooltime = 7 // ダイヤモンドだけ別時間
            }

            else -> {
                return
            }
        }
        point().add(player, point)
        block.setType(Material.BEDROCK)
        // 復活
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable { block.setType(block_type) }, cooltime.toLong() * 20 // クールダウン時間をtick単位に変換
        )
    }
}
