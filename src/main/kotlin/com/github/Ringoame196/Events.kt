package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

class Events(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        // ショップGUIを開く
        val player = e.player
        val entity = e.rightClicked
        val team_name = GET().TeamName(player) ?: return
        if (inspection().shop(entity)) {
            shop().open(e, player, entity as Villager, team_name)
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
        val entity = e.entity
        if (entity is Villager) {
            // ショップがダメージを受けたときの処理
            val damager = e.damager
            if (inspection().shop(entity)) {
                shop().attack(e, damager, entity)
            }
        } else if (entity is org.bukkit.entity.Zombie && entity.scoreboardTags.contains("targetshop")) {
            entity.scoreboardTags.remove("targetshop")
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        // インベントリアイテムクリック
        val player = e.player
        val item = e.item
        val block = e.clickedBlock
        val action = e.action
        if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {
            itemClick().system(player, item, block, e)
        }
    }

    @EventHandler
    fun onBlockBreakEvent(e: BlockBreakEvent) {
        // ブロックを破壊したとき
        val player = e.player
        GameSystem().adventure(e, player)
        val team_name = GET().TeamName(player)
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
        if (inspection().shop(mob)) {
            shop().delete_name(mob.location)
            if (!GET().status()) {
                return
            }
            val blockBelowBlock = mob.location.add(0.0, -1.0, 0.0).block.type
            val winTeam: String? = when (blockBelowBlock) {
                Material.RED_WOOL -> "blue"
                Material.BLUE_WOOL -> "red"
                else -> null
            }
            winTeam?.let { GameSystem().gameend(it) }
        } else {
            if (killer !is Player) { return }
            point().add(killer, 1)
        }
    }

    @EventHandler
    fun onEntityRegainHealthEvent(e: EntityRegainHealthEvent) {
        // ショップが回復したときにHP反映させる
        if (e.entity !is Villager) { return }
        val shop = e.entity as Villager
        val amout = e.amount
        if (inspection().shop(shop)) {
            shop().recovery(shop, amout)
        }
    }

    @EventHandler
    fun onEntityTargetEvent(e: EntityTargetEvent) {
        val player = e.target
        val entity = e.entity

        if (entity.scoreboardTags.contains("targetshop")) {
            e.target = Zombie().getNearestVillager(entity.location, 30.0)
        }
        // 敵対されない帽子
        if (player !is Player) { return }
        val helmet = player.inventory.helmet
        val displayname = helmet?.itemMeta?.displayName
        if (helmet?.type == Material.ZOMBIE_HEAD && displayname == "${ChatColor.GREEN}敵対されない帽子") { e.isCancelled = true }
    }
    @EventHandler
    fun onSignChangeEvent(e: SignChangeEvent) {
        // 看板に文字を決定したとき
        val block = e.block.type == Material.OAK_WALL_SIGN
        if (block) {
            Sign().make(e)
        }
    }

    @EventHandler
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {
        // プレイヤーが抜けたとき
        if (GET().status()) { return }
        val player = e.player
        if (Data.DataManager.gameData.ParticipatingPlayer.contains(player)) {
            Team().inAndout(player)
        }
    }
    @EventHandler
    fun onPlayerRespawn(e: EntityDamageEvent) {
        // ダメージを受けたプレイヤー
        val player = e.entity
        if (player !is Player) { return }

        // ダメージが0以上の場合（リスポーン判定）
        if (e.damage > 0 && player.health <= e.damage) {
            e.isCancelled = true
            Team().respawn(player, plugin)

            // ダメージを与えたエンティティがプレイヤーであればキル処理
            if (e !is EntityDamageByEntityEvent) { return }
            val damager = e.damager
            if (damager !is Player) { return }
            player().kill(damager)
        }
    }
    @EventHandler
    fun onBlockDamage(e: BlockDamageEvent) {
        val player = e.player
        val block = e.block
        point().NotAppropriate(player.inventory.itemInMainHand, block, e)
    }
}
