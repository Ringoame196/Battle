package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.TeamData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.scoreboard.NameTagVisibility

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

    fun inAndout(player: Player) {
        val ParticipatingPlayer = Data.DataManager.gameData.ParticipatingPlayer
        if (GET().status()) {
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
        Sign().Numberdisplay("(参加中:${ParticipatingPlayer.size}人)")
    }
    fun make(name: String, color: ChatColor) {
        Bukkit.getScoreboardManager()?.mainScoreboard?.registerNewTeam(name)
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam(name)?.let {
            it.setAllowFriendlyFire(false)
            it.color = color
            it.nameTagVisibility = NameTagVisibility.ALWAYS
        }
    }
    fun delete() {
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("red")?.unregister()
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("blue")?.unregister()
    }
    fun division() {
        val redTeam = Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("red")
        val blueTeam = Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("blue")
        var team = true
        for (loopPlayer in Data.DataManager.gameData.ParticipatingPlayer) {
            if (team) {
                redTeam?.addPlayer(loopPlayer)
                loopPlayer.teleport(Data.DataManager.LocationData.redspawn!!)
            } else {
                blueTeam?.addPlayer(loopPlayer)
                loopPlayer.teleport(Data.DataManager.LocationData.bluespawn!!)
            }
            team = !team
        }
    }
    fun respawn(player: Player, e: PlayerRespawnEvent) {
        when (GET().TeamName(player)) {
            "red" -> e.respawnLocation = Data.DataManager.LocationData.redspawn!!
            "blue" -> e.respawnLocation = Data.DataManager.LocationData.bluespawn!!
        }
    }
}
