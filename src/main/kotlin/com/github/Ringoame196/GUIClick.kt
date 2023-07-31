package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffectType

class GUIClick {
    fun system(plugin: Plugin, e: InventoryClickEvent, player: Player, GUI_name: String, item: ItemStack) {
        if (GUI_name == "${ChatColor.DARK_GREEN}金床") {
            when (item.type) {
                Material.RED_STAINED_GLASS_PANE -> e.isCancelled = true
                Material.COMMAND_BLOCK -> {
                    anvil().system(player, e.inventory)
                    e.isCancelled = true
                }
                else -> return
            }
        } else if (!GUI_name.contains("[BATTLEGUI]")) { return }
        e.isCancelled = true
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        when (GUI_name.replace("[BATTLEGUI]", "")) {
            "${ChatColor.BLUE}攻防戦ショップ" -> homeshop(player, item)
            "${ChatColor.DARK_GREEN}ショップ" -> shop().system(item, player)
            "${ChatColor.DARK_GREEN}設定画面" -> GameSystem().system(plugin, player, item)
        }
    }

    fun homeshop(player: Player, item: ItemStack) {
        val team_name = GET().getTeamName(player) ?: return
        val item_name = item.itemMeta?.displayName ?: return
        val shop: Inventory = Bukkit.createInventory(null, 36, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        when (item_name) {
            "${ChatColor.YELLOW}共通チェスト" -> {
                // 共有チェストの処理
                player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
                player.openInventory(Data.DataManager.teamDataMap.getOrPut(team_name) { Team() }.chest)
            }
            "${ChatColor.YELLOW}ピッケル" -> GUI().pickaxeshop(shop)
            "${ChatColor.YELLOW}武器" -> GUI().weaponshop(shop)
            "${ChatColor.YELLOW}防具" -> GUI().equipmentshop(shop)
            "${ChatColor.YELLOW}金床" -> anvil().set(player)
            "${ChatColor.YELLOW}チーム強化" -> GUI().potionshop(shop, player)
            "${ChatColor.YELLOW}村人強化" -> GUI().villagerlevelup(shop, player)
            "${ChatColor.YELLOW}その他" -> GUI().general_merchandiseshop(shop, player)
            "${ChatColor.YELLOW}お邪魔アイテム" -> GUI().disturbshop(shop)
            "${ChatColor.YELLOW}ゾンビ" -> GUI().zombieshop(shop)
            else -> return
        }
        shop.getItem(0) ?: return
        player.openInventory(shop)
    }

    fun click_invocation(player: Player, item_name: String, team_name: String) {
        val check_name = item_name
            .replace("${ChatColor.YELLOW}★", "")
            .replace("チーム全員に", "")

        when (check_name) {
            "ショップ解放" -> {
                Data.DataManager.teamDataMap[team_name]?.opening = true
                shop().GUI(player)
                PlayerSend().TeamGiveEffect(player, item_name, null, null, 0, 0)
            }
            "攻撃力UP(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.INCREASE_DAMAGE, null, 2, 180)
            "再生UP(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.REGENERATION, null, 2, 180)
            "採掘速度UP(5分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.FAST_DIGGING, null, 3, 300)
            "耐性(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.DAMAGE_RESISTANCE, null, 1, 180)
            "移動速度UP(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.SPEED, null, 1, 180)
            "攻撃力UP&再生(1分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.REGENERATION, PotionEffectType.INCREASE_DAMAGE, 5, 60)
            "鉱石復活速度UP" -> {
                val set_time = Data.DataManager.teamDataMap.getOrPut(team_name) { Team() }.blockTime - 1
                Data.DataManager.teamDataMap[team_name]?.blockTime = set_time
                GUI().villagerlevelup(player.openInventory.topInventory, player)
                PlayerSend().TeamGiveEffect(player, item_name, null, null, 6 - set_time, 0)
            }
            "村人体力増加" -> {
                val entity = Data.DataManager.teamDataMap[team_name]?.entities?.lastOrNull() as? LivingEntity ?: return
                val maxHPAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)?:return
                // 現在の最大HPを取得
                val currentMaxHP = maxHPAttribute.baseValue + 10.0
                // 最大HPを設定
                maxHPAttribute.baseValue = currentMaxHP
                // 村人の名前を更新（HP表示を変更する場合）
                shop().name(entity as Villager, GET().getHP(entity).toString(), currentMaxHP.toString())
                GUI().villagerlevelup(player.openInventory.topInventory, player)
                PlayerSend().TeamGiveEffect(player, item_name, null, null, 0, 0)
            }
            "盲目(10秒)[妨害]" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.BLINDNESS, null, 255, 10)
            "弱体化(10秒)[妨害]" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.WEAKNESS, null, 255, 10)
            "採掘速度低下(1分)[妨害]" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.SLOW_DIGGING, null, 255, 10)
        }
    }
}
