package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class GUIClick {
    companion object {
        private val teamDataMap: MutableMap<String, Team> = mutableMapOf()
    }

    fun homeshop(player: Player, item: ItemStack) {
        val item_type = item?.type
        val item_name = item?.itemMeta?.displayName
        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        val shop: Inventory = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}ショップ")
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
        } else if (item_type == Material.ANVIL && item_name == "${ChatColor.YELLOW}金床") {
            GUI().enchant_anvil(player)
        } else if (item_type == Material.ENCHANTED_BOOK && item_name == "${ChatColor.YELLOW}エンチャント") {
            GUI().enchantshop(shop, player)
        }
    }
    fun anvil(player: Player, inv: Inventory) {
        var enchantitem = inv.getItem(3)
        val enchant_book = inv.getItem(5)
        if (enchantitem == null) {
            player.sendMessage("${ChatColor.RED}エンチャントするものをセットしてください")
            player.closeInventory()
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
            return
        }
        if (enchantitem?.type.toString().contains("pickaxe")) {} else if (inv.getItem(3)?.type.toString().contains("sword")) {} else {
            player.sendMessage("${ChatColor.RED}対応するものをセットしてください")
            player.closeInventory()
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
            return
        }

        if (enchant_book == null || enchant_book.type != Material.ENCHANTED_BOOK) {
            player.sendMessage("${ChatColor.RED}エンチャント本をセットしてください")
            player.closeInventory()
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
            return
        }

        val itemToEnchant = inv.getItem(3)
        val meta = enchant_book.itemMeta

        if (meta is EnchantmentStorageMeta) {
            val storedEnchants = meta.storedEnchants
            for ((enchantment, level) in storedEnchants) {
                itemToEnchant?.addUnsafeEnchantment(enchantment, level)
            }
        } else {
            val enchants = meta?.enchants
            if (enchants != null) {
                for ((enchantment, level) in enchants) {
                    itemToEnchant?.addUnsafeEnchantment(enchantment, level)
                }
            }
        }
        val newBookItem = ItemStack(Material.AIR)
        inv.setItem(5, newBookItem)
        player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 1f)
    }
}
