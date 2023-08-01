package com.github.Ringoame196

import org.bukkit.Sound
import org.bukkit.entity.Player

class Team {
    fun chest(player: Player, team_name: String) {
        player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
        player.openInventory(Data.DataManager.teamDataMap.getOrPut(team_name) { TeamData() }.chest)
    }
}
