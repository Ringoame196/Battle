package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Suppress("DEPRECATION")
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
        when (item_name) {
            "${ChatColor.YELLOW}ゲーム参加" -> inAndout(player)
            "${ChatColor.RED}ゲーム退出" -> inAndout(player)
            else -> return
        }
        player.closeInventory()
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }
    fun inAndout(player: Player) {
        val ParticipatingPlayer = Data.DataManager.gameData.ParticipatingPlayer
        if (Data.DataManager.gameData.status) {
            PlayerSend().errormessage("ゲームが終わるまでしばらくお待ち下さい", player)
            return
        }
        val message: String = if (ParticipatingPlayer.contains(player)) {
            ParticipatingPlayer.remove(player)
            "退出"
        } else {
            ParticipatingPlayer.add(player)
            "参加"
        }
        val size = "(参加人数:${ParticipatingPlayer.size}人)"
        PlayerSend().participantmessage("${ChatColor.AQUA}[$message] ${player.name}$size")
        player.sendTitle("", "${ChatColor.YELLOW}[${message}しました]")
        Sign().Numberdisplay(ParticipatingPlayer.size)
    }
}
