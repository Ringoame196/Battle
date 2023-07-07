package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.IronGolem
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

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
        Events.DataManager.playerDataMap[player.uniqueId]?.let { playerData ->
            playerData.point += point
            player.sendMessage("${ChatColor.GREEN}+" + point + "p(" + playerData.point + "ポイント)")
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
            itemClick().removeitem(player)
        }
    }
    fun summon_golem(player: Player, type: Material?, name: String) {
        val golem: IronGolem
        val location = player.location

        golem = location.world?.spawn(location, IronGolem::class.java) ?: return
        golem.customName = name
        golem.isCustomNameVisible = true
        golem.isPlayerCreated = true
        if (type == Material.GOLD_BLOCK) {
            golem.health = 1.0
            golem.customName = "${ChatColor.RED}ゴールデンゴーレム"
            golem.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 9999999, 255))
        } else if (type == Material.DIAMOND_BLOCK) {
            golem.health = 500.0
            golem.damage(10.0)
        }
        removeitem(player)
    }
    fun removeitem(player: Player) {
        val itemInHand = player.inventory.itemInMainHand
        val oneItem = itemInHand.clone()
        oneItem.amount = 1
        player.inventory.removeItem(oneItem)
    }
}
