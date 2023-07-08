package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.IronGolem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class itemClick {
    fun summonzombie(player: Player, item: ItemStack?) {
        val item_name = item?.itemMeta?.displayName
        if (item_name?.contains("[召喚]") == false) { return }
        var summon_name = item_name?.replace("[召喚]", "")
        summon_name = summon_name?.replace("${ChatColor.YELLOW}", "")
        var command = "execute as player_name at @s run function akmob:"
        command = command.replace("player_name", player.name)
        if (summon_name == "ノーマルゾンビ") {
            command += "normal"
        } else { return }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
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
        var pointdata = Events.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        pointdata += point
        player.sendMessage("${ChatColor.GREEN}+" + point + "p(" + pointdata + "ポイント)")
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        Events.DataManager.playerDataMap[player.uniqueId]?.let { playerData ->
            playerData.point = pointdata
        }
    }
    fun summon_golem(player: Player, type: Material?, name: String) {
        val location = player.location
        val golem = location.world?.spawn(location, IronGolem::class.java) ?: return

        golem.customName = name
        golem.isCustomNameVisible = true
        golem.isPlayerCreated = true

        when (type) {
            Material.GOLD_BLOCK -> {
                golem.health = 5.0
                golem.customName = "${ChatColor.RED}ゴールデンゴーレム"
                golem.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, Int.MAX_VALUE, 255))
            }
            Material.DIAMOND_BLOCK -> {
                golem.health = 500.0
                golem.damage(10.0)
            }
            else -> return
        }
        player.sendMessage("${ChatColor.YELLOW}ゴーレム召喚")
    }
    fun removeitem(player: Player) {
        val itemInHand = player.inventory.itemInMainHand
        val oneItem = itemInHand.clone()
        oneItem.amount = 1
        player.inventory.removeItem(oneItem)
    }
}
