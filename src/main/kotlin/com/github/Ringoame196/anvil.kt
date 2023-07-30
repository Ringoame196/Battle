package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class anvil {
    fun system(player: Player, inv: Inventory) {
        val enchantitem = inv.getItem(3) ?: return
        val enchantbook = inv.getItem(5) ?: return
        if (enchantbook.type != Material.ENCHANTED_BOOK) {
            errormessage("${ChatColor.RED}エンチャント本をセットしてください(右)", player)
            return
        }
        val enchantitem_name = enchantitem.type.toString()
        val shouldExecute: Boolean
        val supportedItems = listOf("PICKAXE", "SWORD", "CHESTPLATE", "LEGGINGS", "BOOTS")
        shouldExecute = supportedItems.any { enchantitem_name.contains(it) }
        if (!shouldExecute) {
            errormessage("${ChatColor.RED}対応しているアイテムをセットしてください(左)", player)
            return
        }

        val meta = enchantbook.itemMeta
        if (meta is EnchantmentStorageMeta) {
            for ((enchantment, level) in meta.storedEnchants) {
                enchantitem.addUnsafeEnchantment(enchantment, level)
            }
        } else {
            val enchants = meta?.enchants
            enchants?.forEach { (enchantment, level) ->
                enchantitem.addUnsafeEnchantment(enchantment, level)
            }
        }
        inv.setItem(5, ItemStack(Material.AIR))
        player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 1f)
    }
    fun errormessage(message: String, player: Player) {
        player.sendMessage("${ChatColor.RED}$message")
        player.closeInventory()
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }
    fun set(player: Player) {
        val anvil: Inventory = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}金床")
        for (i in 0..7) {
            GUI().set_GUIitem(anvil, i, Material.RED_STAINED_GLASS_PANE, " ", "")
        }
        GUI().set_GUIitem(anvil, 3, Material.AIR, "", "")
        GUI().set_GUIitem(anvil, 5, Material.AIR, "", "")

        GUI().set_GUIitem(anvil, 8, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}合成", "")
        player.openInventory(anvil)
    }
    fun close(player: Player, inventory: Inventory) {
        for (i in 0 until 8) {
            val item = inventory.getItem(i)
            if (item?.type == Material.RED_STAINED_GLASS_PANE) {
                continue
            }
            player.inventory.addItem(item)
        }
    }
}
