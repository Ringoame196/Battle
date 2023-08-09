package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.PlayerData
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.entity.Villager

class GET {
    fun getTeamName(player: Player): String? {
        val teamName = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        return teamName
    }
    fun getJoinTeam(player: Player): Boolean {
        val jointeam = when (getTeamName(player)) {
            "red" -> true
            "blue" -> true
            else -> false
        }
        return jointeam
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
        val OpoposingTeamname = when (TeamName) {
            "red" -> "blue"
            "blue" -> "red"
            else -> null
        }
        return OpoposingTeamname
    }
    fun status(): Boolean {
        return Data.DataManager.gameData.status
    }
    fun locationTitle(location: org.bukkit.Location?): String {
        if (location == null) {
            return "null"
        }

        val x = location.x.toInt()
        val y = location.y.toInt()
        val z = location.z.toInt()

        return "x:$x,y:$y,z:$z"
    }
}
