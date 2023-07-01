package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class GUIClick {
    companion object {
        private val teamDataMap: MutableMap<String, Team> = mutableMapOf()
    }

    fun homeshop(player: Player, item: ItemStack) {
        val item_type = item?.type
        val item_name = item?.itemMeta?.displayName
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        val shop: Inventory = Bukkit.createInventory(null, 18, "${ChatColor.DARK_GREEN}ショップ")
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
        } else if (item_type == Material.IRON_PICKAXE && item_name == "${ChatColor.YELLOW}ピッケル") {
            GUI().pickaxeshop(shop, player)
        } else if (item_type == Material.IRON_SWORD && item_name == "${ChatColor.YELLOW}武器") {
            GUI().weaponshop(shop, player)
        } else if (item_type == Material.IRON_CHESTPLATE && item_name == "${ChatColor.YELLOW}防具") {
            GUI().equipmentshop(shop, player)
        } else if (item_type == Material.ANVIL && item_name == "${ChatColor.YELLOW}金床")
        {
               
        }
    }
}
