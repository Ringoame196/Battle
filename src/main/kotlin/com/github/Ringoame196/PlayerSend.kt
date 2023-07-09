package com.github.Ringoame196

import org.bukkit.Sound
import org.bukkit.entity.Player

class PlayerSend {
    fun playsound(player: Player, sound: Sound) {
        player.playSound(player.location, sound, 1f, 1f)
    }
}
