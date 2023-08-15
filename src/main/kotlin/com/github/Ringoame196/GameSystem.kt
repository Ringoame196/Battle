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
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class GameSystem {
    fun system(plugin: Plugin, player: Player, item: ItemStack, e: InventoryClickEvent) {
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        player.closeInventory()
        val displayName = item.itemMeta?.displayName
        when (displayName) {
            "${ChatColor.AQUA}ゲームスタート" -> start(plugin, player)
            "${ChatColor.RED}終了" -> stop(player)
            "${ChatColor.YELLOW}ショップ召喚" -> shop().summon(player.location, null)
            "${ChatColor.GREEN}参加" -> Team().inAndout(player)
        }
        if (item.type == Material.ENDER_EYE && e.isShiftClick) {
            setlocation(item, player)
        }
    }

    fun start(plugin: Plugin, player: Player) {
        if (Data.DataManager.gameData.ParticipatingPlayer.size == 0) { return }
        if (GET().status()) {
            player.sendMessage("${ChatColor.RED}既にゲームはスタートしています")
            return
        }
        Data.DataManager.gameData.status = true
        reset()
        Data.DataManager.LocationData.let {
            if (it.redshop == null || it.blueshop == null || it.redspawn == null || it.bluespawn == null) {
                NotSet(player)
                return
            }
        }
        shop().summon(Data.DataManager.LocationData.redshop, "red")
        shop().summon(Data.DataManager.LocationData.blueshop, "blue")
        if (Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("red") == null) { Team().make("red", ChatColor.RED) }
        if (Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("blue") == null) { Team().make("blue", ChatColor.BLUE) }
        randomChest().set()
        val location = Data.DataManager.LocationData.randomChest?.add(0.5, -1.0, 0.0)
        val armorStand = location?.let { ArmorStand().summon(it, "") }
        Data.DataManager.gameData.randomChestTitle = armorStand

        var c = 5
        object : BukkitRunnable() {
            override fun run() {
                if (c > 0) {
                    PlayerSend().participantmessage("${ChatColor.GREEN}[カウントダウン]開始まで$c")
                    PlayerSend().participantplaysound(Sound.BLOCK_DISPENSER_FAIL)
                    c--
                } else {
                    Team().division()
                    Sign().Numberdisplay("ゲーム進行中")
                    Bukkit.broadcastMessage("${ChatColor.GREEN}攻防戦ゲームスタート！！")
                    PlayerSend().participantplaysound(Sound.ENTITY_ENDER_DRAGON_AMBIENT)
                    timer(plugin)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L) // 0Lは遅延時間、20Lは繰り返し間隔（1秒=20tick
    }
    fun NotSet(player: Player) {
        player.sendMessage("${ChatColor.RED}未設定の項目があります")
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }
    fun timer(plugin: Plugin) {
        Bukkit.getScheduler().runTaskTimer(
            plugin,
            Runnable {
                if (!GET().status()) { return@Runnable }
                Data.DataManager.gameData.time += 1
                Regularly()
                val randomChestTime = 300 - (Data.DataManager.gameData.time % 300)
                Data.DataManager.gameData.randomChestTitle?.customName = "${ChatColor.AQUA}${GET().minutes(randomChestTime)}"
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
            "${ChatColor.YELLOW}ランダムチェスト" -> randomChest().setLocation(player)
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
        gameEndSystem("${ChatColor.RED}攻防戦ゲーム強制終了！！", null)
    }

    fun gameend(winTeam: String) {
        gameEndSystem("${ChatColor.RED}攻防戦ゲーム終了！！", winTeam)
    }
    fun gameEndSystem(message: String, winTeam: String?) {
        Bukkit.broadcastMessage("${ChatColor.YELLOW}[攻防戦]$message")
        for (loopPlayer in Data.DataManager.gameData.ParticipatingPlayer) {
            loopPlayer.sendMessage("${ChatColor.AQUA}[ゲーム時間]${GET().minutes(Data.DataManager.gameData.time)}")
            loopPlayer.sendMessage("${ChatColor.RED}${winTeam}チームの勝利")

            loopPlayer.playSound(loopPlayer.location, Sound.BLOCK_ANVIL_USE, 1f, 1f)
            loopPlayer.inventory.clear()
            if (loopPlayer.isOp) {
                loopPlayer.inventory.addItem(Give().GameSetting())
                loopPlayer.gameMode = GameMode.CREATIVE
            } else {
                loopPlayer.gameMode = GameMode.ADVENTURE
            }
            for (effect in loopPlayer.activePotionEffects) {
                loopPlayer.removePotionEffect(effect.type)
            }
            Bukkit.getWorld("world")?.let { loopPlayer.teleport(it.spawnLocation) }
            if (winTeam != null) {
                if (winTeam == GET().TeamName(loopPlayer)) {
                    loopPlayer.inventory.addItem(Give().coin(), Give().coin(), Give().coin(), Give().coin(), Give().coin())
                } else {
                    loopPlayer.inventory.addItem(Give().coin())
                }
            }
            Sign().Numberdisplay("(参加中:0人)")
            Data.DataManager.gameData.status = false
            reset()
            Team().make("red", ChatColor.RED)
            Team().make("blue", ChatColor.BLUE)
        }
    }

    fun adventure(e: org.bukkit.event.Event, player: Player) {
        if (player.gameMode == GameMode.CREATIVE) { return }
        if (e is Cancellable) { e.isCancelled = true }
    }
    fun reset() {
        for (entity in Bukkit.getWorld("BATTLE")?.entities!!) {
            if (entity !is Player) {
                entity.remove()
            }
        }
        Data.DataManager.teamDataMap.clear() // teamDataMap を空にする
        Data.DataManager.playerDataMap.clear() // playerDataMap を空にする
        Team().delete()
        randomChest().reset()
        if (GET().status()) { return }
        Data.DataManager.gameData = Gamedata() // gameData を新しい Gamedata インスタンスに置き換える
    }
    fun Regularly() {
        val time = Data.DataManager.gameData.time
        when {
            time % 300 == 0 -> {
                randomChest().set()
            }
            else -> {}
        }
    }
}
