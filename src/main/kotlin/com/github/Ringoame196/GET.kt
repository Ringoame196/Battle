package com.github.Ringoame196

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.entity.Villager

class GET {
    fun getTeamName(player: Player): String? {
        val teamName = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        return teamName
    }
    fun getMaxHP(shop: Villager): Double? {
        val maxHP = shop.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value
        return maxHP
    }
    fun getHP(shop: Villager): Double {
        val HP = shop.health
        return HP
    }
    fun getpoint(player: Player): Int {
        val point = Data.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        return point
    }
    fun getOpposingTeamname(TeamName: String): String? {
        val OpoposingTeamname = when(TeamName){
            "red" -> "blue"
            "blue" -> "red"
            else -> null
        }
        return OpoposingTeamname
    }
}
