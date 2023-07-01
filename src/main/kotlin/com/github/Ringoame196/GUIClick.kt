package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class GUIClick {
    companion object {
        private val teamDataMap: MutableMap<String, Team> = mutableMapOf()
    }

    fun homeshop(player: Player, item: ItemStack) {
        val item_type = item?.type
        val item_name = item?.itemMeta?.displayName
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        if (item_type == Material.CHEST && item_name == "${ChatColor.YELLOW}共通チェスト") {
            // 共有チェストの処理
            if (team_name == null) {
                return
            }

            if (team_name != "red" && team_name != "blue") {
                return
            }
            var chest = teamDataMap.getOrPut(team_name) { Team() }.chest
            player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
            player.openInventory(chest)
        } else if (item_type == Material.ENCHANTED_BOOK) {
        }
    }

    fun setting(e: InventoryClickEvent, item: ItemStack, player: Player) {
        val item_name = item.itemMeta?.displayName
        if (item.type == Material.COBBLESTONE) {
            if (item_name == "${ChatColor.RED}赤 NPC") {
            } else if (item_name == "${ChatColor.BLUE}青 NPC") {
            } else if (item_name == "${ChatColor.RED}赤 ゾンビ") {
            } else if (item_name == "${ChatColor.BLUE}青 ゾンビ") {
            } else if (item_name == "${ChatColor.RED}赤 スポーン") {
            } else if (item_name == "${ChatColor.BLUE}青 スポーン") {
            }
            itemClick().setting(player)
        } else if (item.type == Material.COMMAND_BLOCK) {
        }
    }
}
