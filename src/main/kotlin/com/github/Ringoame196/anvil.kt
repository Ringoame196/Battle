package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class anvil {
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
