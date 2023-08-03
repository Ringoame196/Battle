package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class GameSystem {
    fun system(plugin: Plugin, player: Player, item: ItemStack) {
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        player.closeInventory()
        when (item.itemMeta?.displayName) {
            "${ChatColor.AQUA}ゲームスタート" -> start(plugin, player)
            "${ChatColor.RED}終了" -> stop(player)
            "${ChatColor.YELLOW}ショップ召喚" -> shop().summon(player.location)
        }
    }

    fun start(plugin: Plugin, player: Player) {
        if (Data.DataManager.gameData.status) {
            player.sendMessage("${ChatColor.RED}既にゲームはスタートしています")
            return
        }
        Bukkit.broadcastMessage("${ChatColor.GREEN}攻防戦ゲームスタート！！")
        Data.DataManager.gameData.status = true
        Bukkit.getScheduler().runTaskTimer(
            plugin,
            Runnable {
                if (!Data.DataManager.gameData.status) { return@Runnable }
                Data.DataManager.gameData.time += 1
                Schedule(Data.DataManager.gameData.time)
            },
            0L, 20L
        )
    }

    fun stop(player: Player) {
        if (!Data.DataManager.gameData.status) {
            player.sendMessage("${ChatColor.RED}ゲームは開始していません")
            return
        }
        gameendSystem("${ChatColor.RED}攻防戦ゲーム強制終了！！")
    }

    fun gameend() {
        gameendSystem("${ChatColor.RED}攻防戦ゲーム終了！！")
    }
    fun gameendSystem(message: String) {
        Bukkit.broadcastMessage(message)
        PlayerSend().participantmessage("${ChatColor.YELLOW}[ゲーム時間]" + Data.DataManager.gameData.time + "秒")
        reset()
    }

    fun adventure(e: org.bukkit.event.Event, player: Player) {
        if (GET().getJoinTeam(player)) {
            return
        }
        if (player.gameMode == GameMode.CREATIVE) {
            return
        }
        if (e is Cancellable) {
            e.isCancelled = true
        }
    }
    fun reset() {
        Data.DataManager.teamDataMap.clear() // teamDataMap を空にする
        Data.DataManager.playerDataMap.clear() // playerDataMap を空にする
        Data.DataManager.gameData = Gamedata() // gameData を新しい Gamedata インスタンスに置き換える
    }

    fun Schedule(time: Int) {
        when (time) {
        }
    }
}
