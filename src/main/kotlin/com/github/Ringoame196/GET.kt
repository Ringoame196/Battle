package com.github.Ringoame196

import org.bukkit.entity.Player

class GET {
    fun getTeamName(player: Player): String? {
        val teamName = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        return teamName
    }
}
