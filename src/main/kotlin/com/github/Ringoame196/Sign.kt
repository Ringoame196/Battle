package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.SignChangeEvent

class Sign() {
    fun make(e: SignChangeEvent) {
        val lines = e.lines
        if (lines[0] == "[BATTLE]") {
            e.setLine(0, "${ChatColor.AQUA}[BATTLE]")
            e.setLine(2, "${ChatColor.YELLOW}クリックで参加")
        }
    }
    fun click(player: Player, sign: String) {
        if (sign == ("${ChatColor.AQUA}[BATTLE]")) {
            Team().inAndout(player)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        }
    }
}
