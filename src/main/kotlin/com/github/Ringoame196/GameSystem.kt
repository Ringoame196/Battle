package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.Gamedata
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class GameSystem {
    fun system(plugin: Plugin, player: Player, item: ItemStack) {
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        player.closeInventory()
        val displayName = item.itemMeta?.displayName
        when (displayName) {
            "${ChatColor.AQUA}ゲームスタート" -> start(plugin, player)
            "${ChatColor.RED}終了" -> stop(player)
            "${ChatColor.YELLOW}ショップ召喚" -> shop().summon(player.location)
        }
        if (item.type == Material.ENDER_EYE) {
            setlocation(item, player)
        }
    }

    fun start(plugin: Plugin, player: Player) {
        if (GET().status()) {
            player.sendMessage("${ChatColor.RED}既にゲームはスタートしています")
            return
        }
        Data.DataManager.gameData.status = true
        Data.DataManager.LocationData.let { locationData ->
            locationData.redshop?.let { shop().summon(it) }
            locationData.blueshop?.let { shop().summon(it) }
        }
        Sign().Numberdisplay(0)
        PlayerSend().participantmessage("${ChatColor.GREEN}攻防戦ゲームスタート！！")
        PlayerSend().participantplaysound(Sound.ENTITY_ENDER_DRAGON_AMBIENT)
        Bukkit.getScheduler().runTaskTimer(
            plugin,
            Runnable {
                if (!GET().status()) { return@Runnable }
                Data.DataManager.gameData.time += 1
            },
            0L, 20L
        )
    }
    fun setlocation(item: ItemStack, player: Player) {
        when (item.itemMeta?.displayName) {
            "${ChatColor.RED}shop" -> Data.DataManager.LocationData.redshop = player.location
            "${ChatColor.BLUE}shop" -> Data.DataManager.LocationData.blueshop = player.location
            "${ChatColor.RED}spawn" -> Data.DataManager.LocationData.redspawn = player.location
            "${ChatColor.BLUE}spawn" -> Data.DataManager.LocationData.bluespawn = player.location
        }
        player.sendMessage("${ChatColor.AQUA}座標設定完了")

        val filePath = "plugins/Battle/location_data.yml"
        Data.DataManager.LocationData.saveToFile(filePath)
    }
    fun stop(player: Player) {
        if (!GET().status()) {
            player.sendMessage("${ChatColor.RED}ゲームは開始していません")
            return
        }
        gameEndSystem("${ChatColor.RED}攻防戦ゲーム強制終了！！")
    }

    fun gameend() {
        gameEndSystem("${ChatColor.RED}攻防戦ゲーム終了！！")
    }
    fun gameEndSystem(message: String) {
        PlayerSend().participantmessage(message)
        PlayerSend().participantmessage("${ChatColor.YELLOW}[ゲーム時間]" + Data.DataManager.gameData.time + "秒")
        PlayerSend().participantplaysound(Sound.BLOCK_ANVIL_USE)
        reset()
    }

    fun adventure(e: org.bukkit.event.Event, player: Player) {
        if (!GET().JoinTeam(player)) { return }
        if (player.gameMode == GameMode.CREATIVE) { return }
        if (e is Cancellable) { e.isCancelled = true }
    }
    fun reset() {
        for (shop in Data.DataManager.gameData.shoplist) {
            shop.remove()
        }
        Data.DataManager.teamDataMap.clear() // teamDataMap を空にする
        Data.DataManager.playerDataMap.clear() // playerDataMap を空にする
        Data.DataManager.gameData = Gamedata() // gameData を新しい Gamedata インスタンスに置き換える
    }
}
