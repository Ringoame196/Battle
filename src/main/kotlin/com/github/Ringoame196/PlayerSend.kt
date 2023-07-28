package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

class PlayerSend {
    fun playsound(player: Player, sound: Sound) {
        player.playSound(player.location, sound, 1f, 1f)
    }
    fun participantmessage(message: String) {
        for (loopPlayer in Bukkit.getServer().onlinePlayers) {
            val team = GET().getTeamName(loopPlayer)
            if (team == "red" || team == "blue") {
                loopPlayer.sendMessage(message)
            }
        }
    }
}
