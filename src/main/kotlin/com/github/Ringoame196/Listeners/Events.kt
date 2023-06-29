package com.github.Ringoame196.Listeners

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.Plugin
import java.util.UUID

class Events(private val plugin: Plugin) : Listener {
    private val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()

    fun set_GUIitem(GUI: Inventory, number: Int, set_item: Material, displayname: String) {
        var item = ItemStack(set_item)
        var itemMeta: ItemMeta? = item.itemMeta
        itemMeta?.setDisplayName(displayname)
        item.setItemMeta(itemMeta)
        GUI.setItem(number, item)
    }
    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        val player = e.player
        val entity = e.rightClicked
        if (!(entity is Villager)) {
            return
        }
        if (entity.name != "${ChatColor.GOLD}攻防戦ショップ") {
            return
        }
        // ショップGUI(ホーム)
        e.isCancelled = true
        val shop = Bukkit.createInventory(null, 27, ChatColor.BLUE.toString() + "攻防戦ショップ")

        var point = playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        set_GUIitem(shop, 0, Material.EMERALD, "${ChatColor.GREEN}所持ポイント:" + point + "p")
        set_GUIitem(shop, 1, Material.IRON_PICKAXE, "${ChatColor.YELLOW}ピッケル")
        set_GUIitem(shop, 3, Material.IRON_SWORD, "${ChatColor.YELLOW}剣")
        set_GUIitem(shop, 5, Material.IRON_CHESTPLATE, "${ChatColor.YELLOW}装備")
        set_GUIitem(shop, 7, Material.ENCHANTED_BOOK, "${ChatColor.YELLOW}エンチャント")
        set_GUIitem(shop, 10, Material.POTION, "${ChatColor.YELLOW}ポーション")
        set_GUIitem(shop, 12, Material.REINFORCED_DEEPSLATE, "${ChatColor.YELLOW}強化")
        set_GUIitem(shop, 14, Material.ZOMBIE_SPAWN_EGG, "${ChatColor.YELLOW}ゾンビ")
        set_GUIitem(shop, 16, Material.BEACON, "${ChatColor.YELLOW}その他")

        val no_set = Material.OAK_SIGN
        val no_set_title = "${ChatColor.YELLOW}近日公開"
        set_GUIitem(shop, 19, no_set, no_set_title)
        set_GUIitem(shop, 21, no_set, no_set_title)
        set_GUIitem(shop, 23, no_set, no_set_title)
        set_GUIitem(shop, 25, no_set, no_set_title)

        player.openInventory(shop)
    }

    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        if (e.view.title != "${ChatColor.BLUE}攻防戦ショップ") {
            return
        }
        val player = e.whoClicked as Player
        e.isCancelled = true
        var point = playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        player.sendMessage(point.toString())
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        // ショップがダメージを受けたときの処理
        val entity = e.entity
        val damager = e.damager

        if (!(entity is Villager)) {
            return
        }
        val villager = entity as Villager
        if (villager.customName != "${ChatColor.GOLD}攻防戦ショップ") {
            return
        }

        if (damager is Player) {
            // プレイヤーが殴るのを禁止させる
            val player = damager as Player
            if (player.gameMode == GameMode.CREATIVE) {
                return
            }

            e.isCancelled = true
            return
        }
        // ダメージを受けたときにメッセージを出す
        val message = "${ChatColor.RED}ショップがダメージを食らっています (残りHP" + villager.health + ")"
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
            val zombie = damager as Zombie
            if (team_name == set_team_name) {
                player.sendMessage(message)
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f)
            }
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        // ゾンビ召喚β
        val player = e.player
        val item = e.item
        if (item?.type != Material.SLIME_BALL) {
            return
        }
        if (item?.itemMeta?.displayName != "ゾンビ召喚") {
            return
        }
        // ゾンビのカスタム設定
        val location = Location(player.world, -536.0, 85.0, 370.0) // 召喚する位置を指定
        val zombie = player.world?.spawnEntity(location, EntityType.ZOMBIE)
        zombie?.customName = "MyZombie" // ゾンビの名前
        zombie?.isCustomNameVisible = true // ゾンビの名前を表示するかどうか
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
        val block = e.block.type
        var point = playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        if (block == Material.COAL_ORE) {
            point += 1
        } else if (block == Material.IRON_ORE) {
            point += 3
        } else if (block == Material.GOLD_ORE) {
            point += 5
        } else if (block == Material.DIAMOND_ORE) {
            point += 100
        } else {
            return
        }
        e.isCancelled = true
        player.sendMessage("${ChatColor.AQUA}[現在]$point P")
        playerDataMap[player.uniqueId]?.let { playerData ->
            playerData.point = point
        }
    }
}
