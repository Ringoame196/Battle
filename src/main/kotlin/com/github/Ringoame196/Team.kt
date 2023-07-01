package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.inventory.Inventory

data class Team(
    val blockTime: Int = 5,
    val chest: Inventory = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}チームチェスト"),
)
