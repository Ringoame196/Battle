package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Sign
import org.bukkit.entity.IronGolem
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class itemClick {
    fun system(player: Player, item: ItemStack?, block: Block?, e: PlayerInteractEvent) {
        val item_name = item?.itemMeta?.displayName.toString()
        val item_type = item?.type
        when {
            item_type == Material.SLIME_BALL && item_name.contains("[召喚]") -> {
                Zombie().summon(player, item_name)
            }
            item_type == Material.EMERALD -> {
                money(player, item_name)
            }
            item_name.contains("ゴーレム") -> {
                e.isCancelled = true
                summon_golem(player, item?.type, item_name)
            }
            item_type == Material.COMMAND_BLOCK && item_name == "ゲーム設定" -> {
                e.isCancelled = true
                GUI().gamesettingGUI(player)
            }
            block?.type == Material.OAK_WALL_SIGN -> {
                e.isCancelled = true
                Sign().click(player, block)
                return
            }
            else -> return
        }
        removeitem(player)
    }
    fun money(player: Player, item_name: String) {
        val point: Int
        point = when (item_name) {
            "${ChatColor.GREEN}10p" -> {
                10
            }
            "${ChatColor.GREEN}100p" -> {
                100
            }
            else -> { return }
        }
        point().add(player, point)
    }
    fun summon_golem(player: Player, type: Material?, name: String) {
        val location = player.location
        val golem = location.world?.spawn(location, IronGolem::class.java) ?: return

        golem.customName = name
        golem.isCustomNameVisible = true
        golem.isPlayerCreated = true

        when (type) {
            Material.GOLD_BLOCK -> {
                golem.health = 5.0
                golem.customName = "${ChatColor.RED}ゴールデンゴーレム"
                golem.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, Int.MAX_VALUE, 255))
            }
            Material.DIAMOND_BLOCK -> {
                golem.health = 500.0
                golem.damage(10.0)
            }
            else -> return
        }
        player.sendMessage("${ChatColor.YELLOW}ゴーレム召喚")
    }
    fun removeitem(player: Player) {
        if (player.gameMode == GameMode.CREATIVE) { return }
        val itemInHand = player.inventory.itemInMainHand
        val oneItem = itemInHand.clone()
        oneItem.amount = 1
        player.inventory.removeItem(oneItem)
    }
}
