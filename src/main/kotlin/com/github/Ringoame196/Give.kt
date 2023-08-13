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
    fun Equipment(player: Player, item: ItemStack) {
        val type = item.type.toString()
        if (type.contains("HELMET")) {
            player.inventory.helmet = item
        } else if (type.contains("CHESTPLATE")) {
            player.inventory.chestplate = item
        } else if (type.contains("CHESTPLATE")) {
            player.inventory.chestplate = item
        } else if (type.contains("LEGGINGS")) {
            player.inventory.leggings = item
        } else if (type.contains("BOOTS")) {
            player.inventory.boots = item
        }
    }
    fun Sword(player: Player) {
        player.inventory.remove(Material.WOODEN_SWORD)
        player.inventory.remove(Material.STONE_SWORD)
        player.inventory.remove(Material.IRON_SWORD)
        player.inventory.remove(Material.DIAMOND_SWORD)
    }
    fun Pickaxe(player: Player) {
        player.inventory.remove(Material.WOODEN_PICKAXE)
        player.inventory.remove(Material.STONE_PICKAXE)
        player.inventory.remove(Material.IRON_PICKAXE)
        player.inventory.remove(Material.DIAMOND_PICKAXE)
    }
}
