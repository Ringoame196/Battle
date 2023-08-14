package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.entity.Zombie

class Zombie {
    fun getNearestVillager(location: org.bukkit.Location, radius: Double): Villager? {
        var nearestVillager: Villager? = null
        var nearestDistanceSquared = Double.MAX_VALUE

        for (entity in location.world!!.getNearbyEntities(location, radius, radius, radius)) {
            if (entity.type == EntityType.VILLAGER && entity is Villager) {
                val villager = entity
                val distanceSquared = villager.location.distanceSquared(location)

                if (distanceSquared < nearestDistanceSquared) {
                    nearestDistanceSquared = distanceSquared
                    nearestVillager = villager
                }
            }
        }

        return nearestVillager
    }
    fun summon(player: Player, item_name: String) {
        if (player.location.subtract(0.0, 1.0, 0.0).block.type != Material.GLASS) {
            player.sendMessage("${ChatColor.RED}ガラスの上で実行してください")
            return
        }

        var summon_name = item_name.replace("[召喚]", "")
        summon_name = summon_name.replace("${ChatColor.YELLOW}", "")

        val world = player.world
        val location = player.getLocation()
        location.add(0.0, -3.0, 0.0)
        val zombie: Zombie = world.spawn(location, org.bukkit.entity.Zombie::class.java)
        zombie.scoreboardTags.add("targetshop")
        zombie.scoreboardTags.add("BATTLEmob")
        Data.DataManager.gameData.killmob.add(zombie)

        var command = "execute as ${zombie.uniqueId} at @s run function akmob:"
        command += when (summon_name) {
            "ノーマルゾンビ" -> "normal"
            "チビゾンビ" -> "chibi"
            "シールドゾンビ" -> "shield"
            else -> {
                zombie.remove()
                return // 不明な召喚名の場合は何もせずに処理が終了します
            }
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }
}