package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin
import java.util.UUID

class Events(private val plugin: Plugin) : Listener {
    private val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()
    private val teamDataMap: MutableMap<String, Team> = mutableMapOf()
    val NPC_name = "${ChatColor.GOLD}攻防戦ショップ"
    val guiclass = GUI()

    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        // ショップGUIを開く
        val player = e.player
        val entity = e.rightClicked
        if (!(entity is Villager)) {
            return
        }
        if (!entity.scoreboardTags.contains("shop")) {
            return
        }
        // ショップGUI(ホーム)
        e.isCancelled = true
        val shop = Bukkit.createInventory(null, 27, ChatColor.BLUE.toString() + "攻防戦ショップ")
        val point = playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        guiclass.home(shop, point)
        player.openInventory(shop)
    }

    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        // GUIクリック
        val player = e.whoClicked as Player
        val item = e.currentItem
        val GUIclick = GUIClick()
        val GUI_name = e.view.title

        if (item == null) {
            return
        }

        if (GUI_name == "${ChatColor.BLUE}攻防戦ショップ") {
            GUIclick.homeshop(player, item)
            e.isCancelled = true
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        } else if (GUI_name == "${ChatColor.DARK_GREEN}ショップ") {
            e.isCancelled = true
            val price = item.itemMeta?.lore?.get(0) // 値段取得
            var price_int = 0
            var point = playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point

            for (i in 1..10000) {
                if (price == i.toString() + "p") {
                    price_int = i
                    break
                }
            }

            if (price_int > point) {
                player.sendMessage("${ChatColor.RED}値段が足りません")
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
                player.closeInventory()
            } else {
                point -= price_int
                player.playSound(player, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f)
                val meta = item.itemMeta
                meta?.lore = null
                item.setItemMeta(meta)
                player.inventory.addItem(item)
                playerDataMap[player.uniqueId]?.let { playerData ->
                    playerData.point = point
                }
            }
        } else {
            return
        }
    }

    @EventHandler
    fun onInventoryCloseEvent(e: InventoryCloseEvent) {
        // インベントリを閉じたときの処理
        val player = e.player as Player
        val inventory = e.view
        if (inventory.title != "${ChatColor.DARK_GREEN}チームチェスト") {
            return
        }
        player.playSound(player, Sound.BLOCK_CHEST_CLOSE, 1f, 1f)
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        // ショップがダメージを受けたときの処理
        val entity = e.entity
        val damager = e.damager

        if (!(entity is Villager)) {
            return
        }
        val villager = entity
        if (!villager.scoreboardTags.contains("shop")) {
            return
        }

        if (damager is Player) {
            // プレイヤーが殴るのを禁止させる
            val player = damager
            if (player.gameMode == GameMode.CREATIVE) {
                return
            }

            e.isCancelled = true
            return
        }
        // ダメージを受けたときにメッセージを出す
        var health = villager.health - e.damage
        if (health <= 0) {
            health = 0.0
        }
        val message = "${ChatColor.RED}ショップがダメージを食らっています (残りHP" + health + ")"
        villager.customName = NPC_name + " ${ChatColor.RED}" + health + "HP"
        val blockBelow = villager.location.subtract(0.0, 1.0, 0.0).block.type
        var set_team_name = ""
        if (blockBelow == Material.RED_WOOL) {
            set_team_name = "red"
        } else if (blockBelow == Material.BLUE_WOOL) {
            set_team_name = "blue"
        } else {
            return
        }

        for (player in Bukkit.getServer().onlinePlayers) {
            val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
            if (team_name == set_team_name) {
                player.sendMessage(message)
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f)
            }
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        // インベントリアイテムクリック
        val itemClick = itemClick()
        val player = e.player
        val item = e.item
        val item_type = item?.type
        if (item_type == Material.SLIME_BALL) { // ゾンビ召喚
            itemClick.summonzombie(player, item)
        } else if (item_type == Material.ENDER_EYE) { // 設定
            if (item.itemMeta?.displayName != "攻防戦設定") { return }
            if (!(player.isOp)) { return }
            e.isCancelled = true
            itemClick.setting(player)
        }
    }

    @EventHandler
    fun onBlockBreakEvent(e: BlockBreakEvent) {
        // ブロックを破壊したとき
        val player = e.player
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        if (team_name == null) {
            return
        }
        if (team_name != "red" && team_name != "blue") {
            return
        }

        val block = e.block
        val block_type = block.type
        var point = playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        var cooltime = teamDataMap.getOrPut(team_name) { Team() }.blockTime
        if (block_type == Material.COAL_ORE) {
            point += 1
        } else if (block_type == Material.IRON_ORE) {
            point += 3
        } else if (block_type == Material.GOLD_ORE) {
            point += 5
        } else if (block_type == Material.DIAMOND_ORE) {
            point += 100
            cooltime = 7 // ダイヤモンドだけ別時間
        } else {
            return
        }

        e.isCancelled = true
        player.sendMessage("${ChatColor.AQUA}[現在]$point P")
        playerDataMap[player.uniqueId]?.let { playerData ->
            playerData.point = point
        }
        block.setType(Material.BEDROCK)
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable {
                block.setType(block_type)
            },
            cooltime.toLong() * 20 // クールダウン時間をtick単位に変換
        )
    }
    @EventHandler
    fun onBEntityDeathEvent(e: EntityDeathEvent) {
        // モブをキルしたときの処理
        val player = e.entity.killer as Player
        var point = playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        if (team_name == null) {
            return
        }
        point += 10
        player.sendMessage("${ChatColor.AQUA}[現在]$point P")
        playerDataMap[player.uniqueId]?.let { playerData ->
            playerData.point = point
        }
    }
}
