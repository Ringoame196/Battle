package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
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
    fun summonSystem(player: Player, item_name: String) {
        var summon_name = item_name.replace("[召喚]", "")
        summon_name = summon_name.replace("${ChatColor.YELLOW}", "")

        val world = player.world
        val location = player.getLocation()
        location.add(0.0, -3.0, 0.0)
        val function = when (summon_name) {
            "ノーマルゾンビ" -> "normal"
            "チビゾンビ" -> "chibi"
            "シールドゾンビ" -> "shield"
            "ゾンビソルジャー" -> "soldier"
            "タンクマン" -> "tankman"
            "ダッシュマン" -> "dashman"
            "スケルトンマン" -> "skeletonman"
            "ネザーライトゾンビ" -> "netherite"
            else -> { return }
        }
        summon(location, function)
    }
    fun summon(location: Location, function: String) {
        val world = location.world
        val zombie: Zombie? = world?.spawn(location, org.bukkit.entity.Zombie::class.java)
        zombie?.scoreboardTags?.add("targetshop")
        val command = "execute as ${zombie?.uniqueId} at @s run function akmob:$function"
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }
}
