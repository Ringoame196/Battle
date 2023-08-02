package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Team {
    fun chest(player: Player, team_name: String) {
        player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
        player.openInventory(Data.DataManager.teamDataMap.getOrPut(team_name) { TeamData() }.chest)
    }
    fun fastbreaklevel(team_name: String, player: Player, item_name: String) {
        val set_time = Data.DataManager.teamDataMap.getOrPut(team_name) { TeamData() }.blockTime - 1
        Data.DataManager.teamDataMap[team_name]?.blockTime = set_time
        GUI().villagerlevelup(player.openInventory.topInventory, player)
        PlayerSend().TeamGiveEffect(player, item_name, null, null, 6 - set_time, 0)
    }
    fun GUIClick(player: Player, item: ItemStack) {
        val item_name = item.itemMeta!!.displayName
        val ParticipatingPlayer = Data.DataManager.gameData.ParticipatingPlayer
        when (item_name) {
            "${ChatColor.YELLOW}ゲーム参加" -> {
                ParticipatingPlayer.add(player)
                numberMessage("[参加] ${player.name}")
            }
            "${ChatColor.RED}ゲーム退出" -> {
                ParticipatingPlayer.remove(player)
                numberMessage("[退出] ${player.name}")
            }
            else -> return
        }
        player.closeInventory()
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }
    fun numberMessage(message: String) {
        val size = "(${Data.DataManager.gameData.ParticipatingPlayer.size}人)"
        PlayerSend().participantmessage("${ChatColor.YELLOW}$message $size")
    }
}
