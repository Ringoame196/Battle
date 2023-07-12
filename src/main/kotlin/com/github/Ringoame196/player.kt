package com.github.Ringoame196

import org.bukkit.Sound
import org.bukkit.entity.Player

class player {
    fun kill(killer: Player) {
        point().add(killer, 300)
        killer.playSound(killer, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
    }
}
