package com.github.Ringoame196.data

import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

class Gamedata {
    var status = false
    var time = 0
    var ParticipatingPlayer: MutableList<Player> = mutableListOf()
    var signLocation: org.bukkit.Location? = null
    var randomChestTitle: ArmorStand? = null
}
