package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.UUID

class Events(private val plugin: Plugin) : Listener {
    object DataManager {
        val teamDataMap: MutableMap<String?, Team> = mutableMapOf()
        val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()
    }

    val NPC_name = "${ChatColor.GOLD}攻防戦ショップ"

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
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        DataManager.teamDataMap[team_name]?.entities?.add(entity)
        val shop = Bukkit.createInventory(null, 27, ChatColor.BLUE.toString() + "攻防戦ショップ")
        val point = DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        GUI().home(shop, point)
        player.openInventory(shop)
    }

    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        // GUIクリック
        val player = e.whoClicked as Player
        val teamName = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        val item = e.currentItem
        val item_name = item?.itemMeta?.displayName
        val GUI_name = e.view.title

        if (item == null) {
            return
        }

        if (GUI_name == "${ChatColor.BLUE}攻防戦ショップ") {
            GUIClick().homeshop(player, item)
            e.isCancelled = true
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        } else if (GUI_name == "${ChatColor.DARK_GREEN}ショップ") {
            e.isCancelled = true
            if (item.type == Material.RED_STAINED_GLASS_PANE) {
                return
            }
            val price = item.itemMeta?.lore?.get(0) // 値段取得
            var price_int = 0
            var point = DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point

            for (i in 1..10000) {
                if (price == i.toString() + "p") {
                    price_int = i
                    break
                }
            }
            if (price_int == 0) {
                return
            }

            if (price_int > point) {
                player.sendMessage("${ChatColor.RED}" + (price_int - point) + "ポイント足りません")
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
                player.closeInventory()
            } else {
                point -= price_int
                player.playSound(player, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f)
                DataManager.playerDataMap[player.uniqueId]?.let { playerData ->
                    playerData.point = point
                }
                if (item_name.toString().contains("★")) {
                    GUIClick().click_invocation(player, item_name.toString(), teamName as String)
                    return
                }
                val give_item = ItemStack(item)
                val meta = item.itemMeta
                meta?.lore = null
                give_item.setItemMeta(meta)
                player.inventory.addItem(give_item)
            }
        } else if (GUI_name == "${ChatColor.DARK_GREEN}金床") {
            if (item.type == Material.RED_STAINED_GLASS_PANE) {
                e.isCancelled = true
                return
            }
            if (item.type != Material.COMMAND_BLOCK) {
                return
            }
            e.isCancelled = true
            GUIClick().anvil(player, e.inventory)
        } else {
            return
        }
    }

    @EventHandler
    fun onInventoryCloseEvent(e: InventoryCloseEvent) {
        // インベントリを閉じたときの処理
        val player = e.player as Player
        val inventory = e.view
        val title = inventory.title
        if (title == "${ChatColor.DARK_GREEN}チームチェスト") {
            player.playSound(player, Sound.BLOCK_CHEST_CLOSE, 1f, 1f)
            return
        }
        if (title != "${ChatColor.DARK_GREEN}金床") {
            return
        }
        for (i in 0..8) {
            inventory.getItem(i)?.type?.let { item ->
                when (item) {
                    Material.RED_STAINED_GLASS_PANE -> {
                        // RED_STAINED_GLASS_PANEに関する処理
                    }

                    Material.COMMAND_BLOCK -> {
                        // COMMAND_BLOCKに関する処理
                    }

                    else -> {
                        player.inventory.addItem(e.inventory.getItem(i))
                    }
                }
            }
        }
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        // ショップがダメージを受けたときの処理
        val entity = e.entity
        val damager = e.damager

        if (!(entity is Villager)) {
            return
        }
        if (!entity.scoreboardTags.contains("shop")) {
            return
        }

        if (damager is Player) {
            // プレイヤーが殴るのを禁止させる
            if (damager.gameMode != GameMode.CREATIVE) {
                e.isCancelled = true
            }
            return
        }
        // ダメージを受けたときにメッセージを出す
        var health = entity.health - e.damage
        if (health <= 0) {
            health = 0.0
        }
        val message = "${ChatColor.RED}ショップがダメージを食らっています (残りHP" + health + ")"
        entity.customName = NPC_name + " ${ChatColor.RED}" + health + "HP"
        val blockBelow = entity.location.subtract(0.0, 1.0, 0.0).block.type
        val set_team_name: String
        set_team_name = when (blockBelow) {
            Material.RED_WOOL -> {
                "red"
            }

            Material.BLUE_WOOL -> {
                "blue"
            }

            else -> {
                return
            }
        }

        for (player in Bukkit.getServer().onlinePlayers) {
            val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
            if (team_name != set_team_name) {
                continue
            }
            player.sendMessage(message)
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f)
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        // インベントリアイテムクリック
        val itemClick = itemClick()
        val player = e.player
        val item = e.item
        val item_name = item?.itemMeta?.displayName.toString()
        val item_type = item?.type
        if (!(e.action == Action.RIGHT_CLICK_AIR)) {
            return
        }
        if (item_type == Material.SLIME_BALL) { // ゾンビ召喚
            if (player.location.subtract(0.0, 1.0, 0.0).block.type != Material.GLASS) {
                player.sendMessage("${ChatColor.RED}ガラスの上で実行してください")
                return
            }
            itemClick.summonzombie(player, item)
        } else if (item_type == Material.EMERALD) {
            itemClick().money(player, item_name)
        } else if (item_name.contains("ゴーレム")) {
            e.isCancelled = true
            itemClick().summon_golem(player, item?.type, item_name)
        } else {
            return
        }
        if (player.gameMode == GameMode.CREATIVE) {
            return
        }
        itemClick().removeitem(player)
    }

    @EventHandler
    fun onBlockBreakEvent(e: BlockBreakEvent) {
        // ブロックを破壊したとき
        val player = e.player
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name as String
        if (team_name != "red" && team_name != "blue") {
            return
        }
        if (player.gameMode != GameMode.CREATIVE) {
            e.isCancelled = true
        }

        val block = e.block
        val block_type = block.type
        var point = DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        var cooltime = DataManager.teamDataMap.getOrPut(team_name) { Team() }.blockTime
        when (block_type) {
            Material.COAL_ORE -> {
                point += 1
            }

            Material.IRON_ORE -> {
                point += 3
            }

            Material.GOLD_ORE -> {
                point += 5
            }

            Material.DIAMOND_ORE -> {
                point += 100
                cooltime = 7 // ダイヤモンドだけ別時間
            }

            else -> {
                return
            }
        }

        e.isCancelled = true
        player.sendMessage("${ChatColor.AQUA}[現在]$point P")
        DataManager.playerDataMap[player.uniqueId]?.let { playerData ->
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
    fun onBlockPlaceEvent(e: BlockPlaceEvent) {
        // ブロック設置阻止
        val player = e.player
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        if (team_name != "red" && team_name != "blue") {
            return
        }
        if (player.gameMode != GameMode.CREATIVE) {
            e.isCancelled = true
        }
        val mainitem_name = player.inventory.itemInMainHand.itemMeta?.displayName as String
        if (!mainitem_name.contains("ゴーレム")) {
            return
        }
        player.sendMessage("${ChatColor.RED}ゴーレムを召喚するには ブロックから目線を外してクリック")
    }

    @EventHandler
    fun onEntityDeathEvent(e: EntityDeathEvent) {
        // キル
        val killer = e.entity.killer
        if (killer !is Player) {
            return
        }

        val mob = e.entity
        if (mob !is Player) {
            return
        }

        val point = 300
        val playerData = DataManager.playerDataMap.getOrPut(killer.uniqueId) { PlayerData() }
        playerData.point += point

        killer.sendMessage("${ChatColor.AQUA}[現在] ${playerData.point} P")
        killer.playSound(killer, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
    }

    @EventHandler
    fun onEntityRegainHealthEvent(e: EntityRegainHealthEvent) {
        // ショップが回復したときにHP反映させる
        val shop = e.entity
        if (!(shop is Villager)) {
            return
        }

        if (!shop.scoreboardTags.contains("shop")) {
            return
        }
        val maxHP = shop.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value
        val currentHP = shop.health + e.amount
        val newHP = maxHP?.let { if (currentHP >= it) it else currentHP }

        shop.customName = "$NPC_name ${ChatColor.RED}${newHP}HP"
    }

    @EventHandler
    fun onZombieAggro(e: EntityTargetEvent) {
        // 敵対されない帽子
        val player = e.target as Player
        val helmet = player.inventory.helmet
        if (helmet?.type != Material.ZOMBIE_HEAD) {
            return
        }
        if (helmet.itemMeta?.displayName != "${ChatColor.GREEN}敵対されない帽子") {
            return
        }
        e.isCancelled = true
    }
}
