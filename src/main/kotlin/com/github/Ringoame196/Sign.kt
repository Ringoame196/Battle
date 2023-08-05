package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.Sign
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
    fun click(player: Player, block: Block) {
        val sign = block.state as Sign
        if (sign.lines[0] == ("${ChatColor.AQUA}[BATTLE]")) {
            Data.DataManager.gameData.signLocation = block.location
            Team().inAndout(player)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        }
    }
    fun Numberdisplay(number: Int) {
        val size = "(参加中:${number}人)"
        val sign = Data.DataManager.gameData.signLocation?.block?.state
        if (sign !is Sign) { return }
        sign.lines.set(1, "${ChatColor.GREEN}$size")
        sign.update()
    }
}