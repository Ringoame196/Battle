package com.github.Ringoame196

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Give {
    fun GameSetting(player: Player) {
        val Gamesetting = ItemStack(Material.COMMAND_BLOCK)
        val meta = Gamesetting.itemMeta
        meta?.setDisplayName("ゲーム設定")
        Gamesetting.setItemMeta(meta)
        player.inventory.addItem(Gamesetting)
    }
}
