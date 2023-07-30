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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GUIClick {
    fun system(plugin: Plugin, e: InventoryClickEvent, player: Player, GUI_name: String, item: ItemStack) {
        val item_name = item.itemMeta?.displayName ?: return
        if (GUI_name == "${ChatColor.DARK_GREEN}金床") {
            when (item.type) {
                Material.RED_STAINED_GLASS_PANE -> e.isCancelled = true
                Material.COMMAND_BLOCK -> anvil().system(player, e.inventory)
                else -> return
            }
            e.isCancelled = true
        } else if (!GUI_name.contains("[BATTLEGUI]")) { return }
        e.isCancelled = true
        PlayerSend().playsound(player, Sound.UI_BUTTON_CLICK)
        when (GUI_name.replace("[BATTLEGUI]", "")) {
            "${ChatColor.BLUE}攻防戦ショップ" -> homeshop(player, item)
            "${ChatColor.DARK_GREEN}ショップ" -> shop().system(item, player)
            "${ChatColor.DARK_GREEN}設定画面" -> GameSystem().system(plugin, player, item_name)
        }
    }

    fun homeshop(player: Player, item: ItemStack) {
        val item_type = item.type
        val item_name = item.itemMeta?.displayName
        val team_name = GET().getTeamName(player) ?: return
        val shop: Inventory = Bukkit.createInventory(null, 36, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        when {
            item_type == Material.CHEST && item_name == "${ChatColor.YELLOW}共通チェスト" -> {
                // 共有チェストの処理
                if (team_name !in listOf("red", "blue")) {
                    return
                }
                val chest = Data.DataManager.teamDataMap.getOrPut(team_name) { Team() }.chest
                player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
                player.openInventory(chest)
                return
            }
            item_type == Material.IRON_PICKAXE && item_name == "${ChatColor.YELLOW}ピッケル" -> GUI().pickaxeshop(shop)
            item_type == Material.IRON_SWORD && item_name == "${ChatColor.YELLOW}武器" -> GUI().weaponshop(shop)
            item_type == Material.IRON_CHESTPLATE && item_name == "${ChatColor.YELLOW}防具" -> GUI().equipmentshop(shop)
            item_type == Material.ANVIL && item_name == "${ChatColor.YELLOW}金床" -> {
                anvil().set(player)
                return
            }
            item_type == Material.POTION && item_name == "${ChatColor.YELLOW}チーム強化" -> GUI().potionshop(shop, player)
            item_type == Material.VILLAGER_SPAWN_EGG && item_name == "${ChatColor.YELLOW}村人強化" -> {
                GUI().villagerlevelup(shop, player)
                return
            }
            item_type == Material.BEACON && item_name == "${ChatColor.YELLOW}その他" -> GUI().general_merchandiseshop(shop, player)
            item_type == Material.TNT && item_name == "${ChatColor.YELLOW}お邪魔アイテム" -> GUI().disturbshop(shop)
            item_type == Material.ZOMBIE_SPAWN_EGG && item_name == "${ChatColor.YELLOW}ゾンビ" -> GUI().zombieshop(shop)
            else -> return
        }
        player.openInventory(shop)
    }

    fun click_invocation(player: Player, item_name: String, team_name: String) {
        var check_name = item_name
        var player_team_name = team_name
        check_name = check_name.replace("${ChatColor.YELLOW}", "")
        check_name = check_name.replace("チーム全員に", "")
        check_name = check_name.replace("★", "")

        var effect: PotionEffectType? = null
        var effect2: PotionEffectType? = null
        var level = 0
        var time = 0

        when (check_name) {
            "ショップ解放" -> {
                Data.DataManager.teamDataMap[team_name]?.opening = true
                shop().GUI(player)
            }
            "攻撃力UP(3分)" -> {
                effect = PotionEffectType.INCREASE_DAMAGE
                level = 2
                time = 180
            }
            "再生UP(3分)" -> {
                effect = PotionEffectType.REGENERATION
                level = 2
                time = 180
            }
            "採掘速度UP(5分)" -> {
                effect = PotionEffectType.FAST_DIGGING
                level = 3
                time = 300
            }
            "耐性(3分)" -> {
                effect = PotionEffectType.DAMAGE_RESISTANCE
                level = 1
                time = 180
            }
            "移動速度UP(3分)" -> {
                effect = PotionEffectType.SPEED
                level = 1
                time = 180
            }
            "攻撃力UP&再生(1分)" -> {
                effect = PotionEffectType.REGENERATION
                effect2 = PotionEffectType.INCREASE_DAMAGE
                level = 5
                time = 60
            }
            "鉱石復活速度UP" -> {
                var set_time = Data.DataManager.teamDataMap.getOrPut(team_name) { Team() }.blockTime
                set_time -= 1
                Data.DataManager.teamDataMap[team_name]?.blockTime = set_time
                GUI().villagerlevelup(player.openInventory.topInventory, player)

                level = 6 - set_time
            }
            "村人体力増加" -> {
                val entity = Data.DataManager.teamDataMap[team_name]?.entities?.lastOrNull()

                if (entity !is LivingEntity) { return }
                val maxHPAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                if (maxHPAttribute == null) { return }
                // 現在の最大HPを取得
                val currentMaxHP = maxHPAttribute.baseValue
                // 最大HPを増やす
                val increasedMaxHP = currentMaxHP + 10.0
                // 最大HPを設定
                maxHPAttribute.baseValue = increasedMaxHP

                // 村人の名前を更新（HP表示を変更する場合）
                shop().name(entity as Villager, GET().getHP(entity).toString(), increasedMaxHP.toString())
                GUI().villagerlevelup(player.openInventory.topInventory, player)
            }
            "盲目(10秒)[妨害]" -> {
                effect = PotionEffectType.BLINDNESS
                level = 255
                time = 10
            }
            "弱体化(10秒)[妨害]" -> {
                effect = PotionEffectType.WEAKNESS
                level = 1
                time = 10
            }
            "採掘速度低下(1分)[妨害]" -> {
                effect = PotionEffectType.SLOW_DIGGING
                level = 1
                time = 60
            }
        }

        var message = "${ChatColor.AQUA}[チーム強化]${player.name}さんが${item_name}${ChatColor.AQUA}を発動しました(レベル$level)"
        if (check_name.contains("[妨害]")) {
            // 反対チーム名にする
            message = "${ChatColor.RED}[妨害]${player_team_name}チームが${item_name}${ChatColor.RED}を発動しました(レベル $level)"
            player_team_name = GET().getOpposingTeamname(player_team_name)
        }

        for (loopPlayer in Bukkit.getServer().onlinePlayers) {
            val loopPlayerTeam = GET().getTeamName(loopPlayer)

            if (loopPlayerTeam == player_team_name) {
                loopPlayer.sendMessage(message)
                effect?.let { loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1)) }
                effect2?.let { loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1)) }
                loopPlayer.playSound(loopPlayer.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            } else {
                if (!check_name.contains("[妨害]")) {
                    continue
                }
                player.sendMessage("${ChatColor.RED}${player.name}が妨害発動しました${ChatColor.YELLOW}($item_name)")
            }
        }
    }
}
