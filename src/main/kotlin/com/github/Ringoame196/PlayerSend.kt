package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PlayerSend {
    fun participantmessage(message: String) {
        for (loopplayer in Data.DataManager.gameData.ParticipatingPlayer) {
            loopplayer.sendMessage(message)
        }
    }
    fun errormessage(message: String, player: Player) {
        player.sendMessage("${ChatColor.RED}$message")
        player.closeInventory()
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }
    fun TeamGiveEffect(player: Player, itemName: String, effect1: PotionEffectType? = null, effect2: PotionEffectType? = null, level: Int, time: Int) {
        val playerName = player.name
        var playerTeamName = GET().getTeamName(player)
        var message = "${ChatColor.AQUA}[チーム]${playerName}さんが${itemName}${ChatColor.AQUA}を発動しました(レベル$level)"
        if (itemName.contains("[妨害]")) {
            // 反対チーム名にする
            message = "${ChatColor.RED}[妨害]${playerTeamName}チームが${itemName}${ChatColor.RED}を発動しました(レベル $level)"
            playerTeamName = GET().getOpposingTeamname(playerTeamName!!)
        }

        for (loopPlayer in Bukkit.getServer().onlinePlayers) {
            val loopPlayerTeam = GET().getTeamName(loopPlayer)

            if (loopPlayerTeam == playerTeamName) {
                loopPlayer.sendMessage(message)
                effect1?.let { loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1)) }
                effect2?.let { loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1)) }
                loopPlayer.playSound(loopPlayer.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            } else {
                if (itemName.contains("[妨害]")) {
                    player.sendMessage("${ChatColor.RED}${playerName}が妨害発動しました${ChatColor.YELLOW}($itemName)")
                }
            }
        }
    }
}
