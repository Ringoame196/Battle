package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.entity.Zombie

class Zombie {
    fun getNearestVillager(location: Location, radius: Double): Villager? {
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
            "シャーマン" -> "shaman"
            else -> { return }
        }
        summon(location, function)
    }
    @Suppress("DEPRECATION")
    fun summon(location: Location, function: String) {
        val world = location.world
        val zombie: Zombie? = world?.spawn(location, org.bukkit.entity.Zombie::class.java)
        val command = "execute as ${zombie?.uniqueId} at @s run function akmob:$function"
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
        zombie?.scoreboardTags?.add("targetshop")
        if (function == "netherite") {
            zombie?.maxHealth = 500.0
            zombie?.health = 500.0
        }
        zombie?.let { Data.DataManager.gameData.zombie.add(it) }
    }
    fun breakcheck() {
        for (zombie in Data.DataManager.gameData.zombie) {
            if (Bukkit.getWorld("BATTLE")?.entities?.contains(zombie) == false) {
                Data.DataManager.gameData.zombie.remove(zombie)
                continue
            }
            val location = zombie.location
            breakFence(location)
        }
    }
    fun breakFence(location: Location) {
        for (xOffset in -1..1) {
            for (yOffset in -1..1) {
                for (zOffset in -1..1) {
                    val block = location.clone().add(xOffset.toDouble(), yOffset.toDouble(), zOffset.toDouble()).block
                    if (block.type != Material.OAK_FENCE) { continue }
                    block.type = Material.AIR
                    location.world?.playSound(location, Sound.BLOCK_BELL_USE, 1f, 1f)
                    return
                }
            }
        }
    }
}
