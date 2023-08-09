package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.TeamData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class point {
    fun set(player: Player, setpoint: Int) {
        Data.DataManager.playerDataMap[player.uniqueId]?.point = setpoint
    }
    fun add(player: Player, addpoint: Int) {
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
        val point = GET().point(player) + addpoint
        player.sendMessage("${ChatColor.GREEN}+$addpoint (${point}ポイント)")
        set(player, point)
    }
    fun remove(player: Player, removepoint: Int) {
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        var point = GET().point(player)
        point -= removepoint
        set(player, point)
    }
    fun ore(e: org.bukkit.event.Event, player: Player, block: Block, team: String, plugin: Plugin) {
        val block_type = block.type
        GameSystem().adventure(e, player)
        var cooltime = Data.DataManager.teamDataMap.getOrPut(team) { TeamData() }.blockTime
        val point: Int
        when (block_type) {
            Material.COAL_ORE -> point = 1
            Material.IRON_ORE -> point = 3
            Material.GOLD_ORE -> point = 5
            Material.DIAMOND_ORE -> {
                point = 100
                cooltime = 7 // ダイヤモンドだけ別時間
            }
            else -> return
        }
        point().add(player, point)
        block.setType(Material.BEDROCK)
        // 復活
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable { block.setType(block_type) }, cooltime.toLong() * 20 // クールダウン時間をtick単位に変換
        )
    }
    fun purchase(player: Player, price: String): Boolean {
        val price_int: Int = price.replace("p", "").toInt()
        val point = GET().point(player)
        return if (price_int > point) {
            PlayerSend().errormessage("${ChatColor.RED}" + (price_int - point) + "ポイント足りません", player)
            false
        } else {
            player.playSound(player, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f)
            remove(player, price_int)
            true
        }
    }
}
