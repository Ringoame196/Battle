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
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GUIClick {
    fun system(plugin: Plugin, e: InventoryClickEvent, player: Player, GUI_name: String, item: ItemStack) {
        val item_name = item.itemMeta?.displayName

        when (GUI_name) {
            "${ChatColor.BLUE}攻防戦ショップ" -> {
                GUIClick().homeshop(player, item)
                e.isCancelled = true
                PlayerSend().playsound(player, Sound.UI_BUTTON_CLICK)
            }
            "${ChatColor.DARK_GREEN}ショップ" -> {
                e.isCancelled = true
                if (item.type == Material.RED_STAINED_GLASS_PANE) {
                    return
                }
                val price = item.itemMeta?.lore?.get(0) // 値段取得
                var price_int = 0
                var point = Data.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point

                for (i in 1..10000) {
                    if (price == i.toString() + "p") {
                        price_int = i
                        break
                    }
                }
                if (price_int == 0) {
                    return
                }
                if (price_int > point) {
                    player.sendMessage("${ChatColor.RED}" + (price_int - point) + "ポイント足りません")
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
                    player.closeInventory()
                } else {
                    point -= price_int
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f)
                    Data.DataManager.playerDataMap[player.uniqueId]?.let { playerData ->
                        playerData.point = point
                    }
                    if (item_name?.contains("★") == true) { // null対策
                        val set_team_name = GET().getTeamName(player) ?: return
                        click_invocation(player, item_name, set_team_name)
                        return
                    }
                    val give_item = ItemStack(item)
                    val meta = item.itemMeta
                    meta?.lore = null
                    give_item.setItemMeta(meta)
                    player.inventory.addItem(give_item)
                }
            }
            "${ChatColor.DARK_GREEN}金床" -> {
                if (item.type == Material.RED_STAINED_GLASS_PANE) {
                    e.isCancelled = true
                    return
                }
                if (item.type != Material.COMMAND_BLOCK) {
                    return
                }
                e.isCancelled = true
                GUIClick().anvil(player, e.inventory)
            }
            "${ChatColor.DARK_GREEN}設定画面" -> {
                e.isCancelled = true
                if (item_name != null) {
                    GameSystem().system(plugin, player, item_name)
                    player.closeInventory()
                }
            }
        }
    }

    fun homeshop(player: Player, item: ItemStack) {
        val item_type = item.type
        val item_name = item.itemMeta?.displayName
        val team_name = GET().getTeamName(player) ?: return
        val shop: Inventory = Bukkit.createInventory(null, 36, "${ChatColor.DARK_GREEN}ショップ")
        when {
            item_type == Material.CHEST && item_name == "${ChatColor.YELLOW}共通チェスト" -> {
                // 共有チェストの処理
                if ((team_name != "red" && team_name != "blue")) {
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

    fun anvil(player: Player, inv: Inventory) {
        val enchantitem = inv.getItem(3)
        val enchant_book = inv.getItem(5)
        if (enchantitem == null) {
            player.sendMessage("${ChatColor.RED}エンチャントするものをセットしてください")
            player.closeInventory()
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
            return
        }
        // 金床
        val enchantitem_name = enchantitem.type.toString()
        var shouldExecute = false

        if (enchantitem_name.contains("PICKAXE")) {
            // PICKAXEに関する処理
            shouldExecute = true
        } else if (enchantitem_name.contains("SWORD")) {
            // SWORDに関する処理
            shouldExecute = true
        } else if (enchantitem_name.contains("BOW")) {
            // BOWに関する処理
            shouldExecute = true
        } else if (enchantitem_name.contains("CHESTPLATE")) {
            // CHESTPLATEに関する処理
            shouldExecute = true
        } else if (enchantitem_name.contains("LEGGINGS")) {
            // LEGGINGSに関する処理
            shouldExecute = true
        } else if (enchantitem_name.contains("BOOTS")) {
            // BOOTSに関する処理
            shouldExecute = true
        }
        if (!shouldExecute) {
            player.sendMessage("${ChatColor.RED}対応するものをセットしてください")
            player.closeInventory()
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
            return
        }

        if (enchant_book == null || enchant_book.type != Material.ENCHANTED_BOOK) {
            player.sendMessage("${ChatColor.RED}エンチャント本をセットしてください")
            player.closeInventory()
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
            return
        }

        val itemToEnchant = inv.getItem(3)
        val meta = enchant_book.itemMeta

        if (meta is EnchantmentStorageMeta) {
            val storedEnchants = meta.storedEnchants
            for ((enchantment, level) in storedEnchants) {
                itemToEnchant?.addUnsafeEnchantment(enchantment, level)
            }
        } else {
            val enchants = meta?.enchants
            if (enchants == null) { return }
            for ((enchantment, level) in enchants) {
                itemToEnchant?.addUnsafeEnchantment(enchantment, level)
            }
        }
        val newBookItem = ItemStack(Material.AIR)
        inv.setItem(5, newBookItem)
        player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 1f)
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

        check_name.let {
            when (it) {
                "攻撃力UP(3分)" -> {
                    effect = PotionEffectType.INCREASE_DAMAGE
                    level = 2
                    time = 180
                }
                "再生UP(3分)" -> {
                    effect = PotionEffectType.INCREASE_DAMAGE
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

                    if (entity is LivingEntity) {
                        val maxHPAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                        if (maxHPAttribute != null) {
                            // 現在の最大HPを取得
                            val currentMaxHP = maxHPAttribute.baseValue
                            // 最大HPを増やす
                            val increasedMaxHP = currentMaxHP + 10.0
                            // 最大HPを設定
                            maxHPAttribute.baseValue = increasedMaxHP

                            // 村人の名前を更新（HP表示を変更する場合）
                            shop().name(entity as Villager, "${GET().getHP(entity)}/${increasedMaxHP}HP")
                        }
                    }
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
        }

        var message = "${ChatColor.AQUA}[チーム強化]" + player.name + "さんが" + item_name + "${ChatColor.AQUA}を発動しました(レベル" + level.toString() + ")"
        if (check_name.contains("[妨害]")) {
            // 反対チーム名にする
            player_team_name = if (player_team_name == "red") { "blue" } else { "red" }
            message = "${ChatColor.RED}[妨害]" + player_team_name + "チームが" + item_name + "${ChatColor.RED}を発動しました(レベル" + level.toString() + ")"
        }

        for (loopPlayer in Bukkit.getServer().onlinePlayers) {
            val loopPlayerTeam = loopPlayer.scoreboard.teams.firstOrNull { it.hasEntry(loopPlayer.name) }?.name

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
