package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Material
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
import org.bukkit.plugin.Plugin

class Events(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        // ショップGUIを開く
        val player = e.player
        val entity = e.rightClicked
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name as String
        if (entity is Villager && entity.scoreboardTags.contains("shop")) {
            shop().open(e, player, entity, team_name)
        }
    }

    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        // GUIクリック
        val player = e.whoClicked as Player
        val item = e.currentItem ?: return
        val GUI_name = e.view.title
        GUIClick().system(plugin, e, player, GUI_name, item)
    }

    @EventHandler
    fun onInventoryCloseEvent(e: InventoryCloseEvent) {
        // インベントリを閉じる時のイベント
        val player = e.player as Player
        val title = e.view.title
        val inventory = e.inventory
        GUI().close(title, player, inventory)
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        // ショップがダメージを受けたときの処理
        val entity = e.entity as Villager
        val damager = e.damager

        if (entity.scoreboardTags.contains("shop")) {
            shop().attack(e, damager, entity)
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        // インベントリアイテムクリック
        val player = e.player
        val item = e.item ?: return
        val action = e.action
        if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {
            itemClick().system(player, item, e)
        }
    }

    @EventHandler
    fun onBlockBreakEvent(e: BlockBreakEvent) {
        // ブロックを破壊したとき
        val player = e.player
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name as String
        val block = e.block
        point().ore(e, player, block, team_name, plugin)
    }

    @EventHandler
    fun onBlockPlaceEvent(e: BlockPlaceEvent) {
        // ブロック設置阻止
        val player = e.player
        GameSystem().adventure(e, player)
    }

    @EventHandler
    fun onEntityDeathEvent(e: EntityDeathEvent) {
        // キル
        val killer = e.entity.killer
        val mob = e.entity
        if (killer is Player && mob is Player) {
            player().kill(killer)
        } else if (mob.scoreboardTags.contains("shop")) {
            GameSystem().gameend()
        }
    }

    @EventHandler
    fun onEntityRegainHealthEvent(e: EntityRegainHealthEvent) {
        // ショップが回復したときにHP反映させる
        val shop = e.entity
        val amout = e.amount
        if (shop is Villager && shop.scoreboardTags.contains("shop")) {
            shop().recovery(shop, amout)
        }
    }

    @EventHandler
    fun onZombieAggro(e: EntityTargetEvent) {
        // 敵対されない帽子
        val player = e.target as Player
        val helmet = player.inventory.helmet
        val displayname = helmet?.itemMeta?.displayName
        if (helmet?.type == Material.ZOMBIE_HEAD && displayname == "${ChatColor.GREEN}敵対されない帽子") {
            GameSystem().adventure(e, player)
        }
    }
}
