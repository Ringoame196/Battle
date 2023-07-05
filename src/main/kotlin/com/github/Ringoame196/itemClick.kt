package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack

class itemClick {
    fun summonzombie(player: Player, item: ItemStack?) {
        val item_name = item?.itemMeta?.displayName
        if (item_name?.contains("[召喚]") == false) { return }
        val summon_name = item_name?.replace("[召喚]", "")
        if (summon_name == null) { return }
        // ゾンビのカスタム設定
        player.sendMessage("${ChatColor.AQUA}" + summon_name + "召喚")
        val location = player.location.subtract(0.0, 2.0, 0.0) // 召喚する位置を指定

        val zombie = player.world.spawnEntity(location, EntityType.ZOMBIE) as Zombie
        zombie.customName = summon_name // ゾンビの名前
        zombie.isCustomNameVisible = true // ゾンビの名前を表示するかどうか

        // 装備
        val zombieEquipment = zombie.equipment
        val helmet = ItemStack(Material.ZOMBIE_HEAD)
        zombieEquipment?.helmet = helmet

        if (player.gameMode != GameMode.CREATIVE) {
            removeitem(player)
        }
    }
    fun removeitem(player: Player) {
        val itemInHand: ItemStack = player.inventory.itemInMainHand
        val oneItem: ItemStack = itemInHand.clone()
        oneItem.amount = 1
        player.inventory.removeItem(oneItem)
    }
}
