package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GUI {
    fun set_GUIitem(GUI: Inventory, number: Int, set_item: Material, displayname: String, lore: String) {
        // GUIにアイテムを楽にセットする
        val item = ItemStack(set_item)
        val itemMeta: ItemMeta? = item.itemMeta
        itemMeta?.setDisplayName(displayname)
        lore(itemMeta, lore)
        itemMeta?.isUnbreakable = true // 不破壊
        item.setItemMeta(itemMeta)
        GUI.setItem(number, item)
    }
    fun set_potionGUIitem(GUI: Inventory, number: Int, item: Material, lore: String, typePotion: PotionEffectType, level: Int, time: Int) {
        // GUIにポーションを楽にセットする
        val itemStack = ItemStack(item)
        val potionMeta = itemStack.itemMeta as PotionMeta
        lore(potionMeta, lore)

        // 既存のカスタムエフェクトをクリア
        potionMeta.clearCustomEffects()
        val regenerationEffect = PotionEffect(typePotion, time * 20, level)
        potionMeta.addCustomEffect(regenerationEffect, true)
        itemStack.setItemMeta(potionMeta)
        GUI.setItem(number, itemStack)
    }
    fun set_enchant_GUIitem(GUI: Inventory, number: Int, lore: String, enchant: Enchantment, level: Int) {
        // GUIにエンチャント本を楽にセットする
        val item = ItemStack(Material.ENCHANTED_BOOK)
        val itemMeta: ItemMeta = item.itemMeta!!
        itemMeta.setLore(listOf(lore))
        if (itemMeta is EnchantmentStorageMeta) {
            itemMeta.addStoredEnchant(enchant, level, true)
        }
        item.itemMeta = itemMeta
        GUI.setItem(number, item)
    }

    fun lore(meta: ItemMeta?, lore: String) {
        val loreList: MutableList<String> = mutableListOf()
        loreList.add(lore)
        if (meta?.displayName?.contains("★") == true) {
            loreList.add("")
            loreList.add("クリックで発動")
        }
        meta?.lore = loreList
    }

    fun no_set(GUI: Inventory, number: Int) {
        set_GUIitem(GUI, number, Material.OAK_SIGN, "${ChatColor.YELLOW}近日公開", "")
    }

    fun pickaxeshop(GUI: Inventory) {
        dividing_line(GUI, 9)
        set_GUIitem(GUI, 0, Material.STONE_PICKAXE, "石ピッケル", "5p")
        set_GUIitem(GUI, 1, Material.IRON_PICKAXE, "鉄ピッケル", "20p")
        set_GUIitem(GUI, 2, Material.DIAMOND_PICKAXE, "ダイヤモンドピッケル", "300p")
        set_GUIitem(GUI, 3, Material.NETHERITE_PICKAXE, "ネザライトピッケル", "5000p")
        set_enchant_GUIitem(GUI, 18, "5p", Enchantment.DIG_SPEED, 1)
        set_enchant_GUIitem(GUI, 19, "20p", Enchantment.DIG_SPEED, 2)
        set_enchant_GUIitem(GUI, 20, "300p", Enchantment.DIG_SPEED, 3)
        set_enchant_GUIitem(GUI, 21, "500p", Enchantment.DIG_SPEED, 4)
        set_enchant_GUIitem(GUI, 22, "5000p", Enchantment.DIG_SPEED, 5)
    }
    fun weaponshop(GUI: Inventory) {
        dividing_line(GUI, 9)
        set_GUIitem(GUI, 0, Material.STONE_SWORD, "石の剣", "5p")
        set_GUIitem(GUI, 1, Material.IRON_SWORD, "鉄の剣", "20p")
        set_GUIitem(GUI, 2, Material.DIAMOND_SWORD, "ダイヤモンドの剣", "100p")
        set_GUIitem(GUI, 3, Material.NETHERITE_SWORD, "ネザーライトの剣", "1500p")
        set_GUIitem(GUI, 5, Material.SHIELD, "盾", "500p")
        set_GUIitem(GUI, 6, Material.BOW, "弓", "100p")
        set_GUIitem(GUI, 7, Material.CROSSBOW, "クロスボー", "300p")
        set_GUIitem(GUI, 8, Material.ARROW, "矢", "1p")
        set_enchant_GUIitem(GUI, 18, "20p", Enchantment.DAMAGE_ALL, 1)
        set_enchant_GUIitem(GUI, 19, "100p", Enchantment.DAMAGE_ALL, 2)
        set_enchant_GUIitem(GUI, 20, "300p", Enchantment.DAMAGE_ALL, 3)
        set_enchant_GUIitem(GUI, 21, "500p", Enchantment.DAMAGE_ALL, 4)
        set_enchant_GUIitem(GUI, 27, "20p", Enchantment.DAMAGE_UNDEAD, 1)
        set_enchant_GUIitem(GUI, 28, "100p", Enchantment.DAMAGE_UNDEAD, 2)
        set_enchant_GUIitem(GUI, 29, "300p", Enchantment.DAMAGE_UNDEAD, 3)
        set_enchant_GUIitem(GUI, 30, "500p", Enchantment.DAMAGE_UNDEAD, 4)
    }
    fun equipmentshop(GUI: Inventory) {
        dividing_line(GUI, 18)
        set_GUIitem(GUI, 0, Material.GOLDEN_CHESTPLATE, "金のチェストプレート", "100p")
        set_GUIitem(GUI, 1, Material.GOLDEN_LEGGINGS, "金のレギンス", "100p")
        set_GUIitem(GUI, 2, Material.GOLDEN_BOOTS, "金のブーツ", "100p")
        set_GUIitem(GUI, 4, Material.DIAMOND_CHESTPLATE, "ダイヤモンドのチェストプレート", "2500p")
        set_GUIitem(GUI, 5, Material.DIAMOND_LEGGINGS, "ダイヤモンドのレギンス", "2500p")
        set_GUIitem(GUI, 6, Material.DIAMOND_BOOTS, "ダイヤモンドのブーツ", "1500p")
        set_GUIitem(GUI, 9, Material.IRON_CHESTPLATE, "鉄のチェストプレート", "300p")
        set_GUIitem(GUI, 10, Material.IRON_LEGGINGS, "鉄のレギンス", "300p")
        set_GUIitem(GUI, 11, Material.IRON_BOOTS, "鉄のブーツ", "300p")
        set_GUIitem(GUI, 13, Material.NETHERITE_CHESTPLATE, "ネザーライトのチェストプレート", "10000p")
        set_GUIitem(GUI, 14, Material.NETHERITE_LEGGINGS, "ネザーライトのレギンス", "8000p")
        set_GUIitem(GUI, 15, Material.NETHERITE_BOOTS, "ネザーライトのブーツ", "5000p")
        set_enchant_GUIitem(GUI, 27, "20p", Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        set_enchant_GUIitem(GUI, 28, "100p", Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        set_enchant_GUIitem(GUI, 29, "300p", Enchantment.PROTECTION_ENVIRONMENTAL, 3)
        set_enchant_GUIitem(GUI, 30, "500p", Enchantment.PROTECTION_ENVIRONMENTAL, 4)
    }
    fun potionshop(GUI: Inventory, player: Player) {
        player.openInventory(GUI)
        set_potionGUIitem(GUI, 0, Material.SPLASH_POTION, "20p", PotionEffectType.HEAL, 1, 1)
        set_potionGUIitem(GUI, 1, Material.SPLASH_POTION, "100p", PotionEffectType.REGENERATION, 1, 22)
        dividing_line(GUI, 9)
        set_GUIitem(GUI, 18, Material.RED_DYE, "${ChatColor.YELLOW}★チーム全員に攻撃力UP(3分)", "100p")
        set_GUIitem(GUI, 19, Material.MAGENTA_DYE, "${ChatColor.YELLOW}★チーム全員に再生UP(3分)", "100p")
        set_GUIitem(GUI, 20, Material.ORANGE_DYE, "${ChatColor.YELLOW}★チーム全員に採掘速度UP(5分)", "100p")
        set_GUIitem(GUI, 21, Material.GRAY_DYE, "${ChatColor.YELLOW}★チーム全員に耐性(3分)", "100p")
        set_GUIitem(GUI, 22, Material.LIGHT_BLUE_DYE, "${ChatColor.YELLOW}★チーム全員に移動速度UP(3分)", "100p")
        set_GUIitem(GUI, 23, Material.NETHER_STAR, "${ChatColor.YELLOW}★チーム全員に攻撃力UP&再生(1分)", "500p")
    }
    fun zombieshop(GUI: Inventory) {
        set_GUIitem(GUI, 0, Material.SLIME_BALL, "${ChatColor.YELLOW}[召喚]ノーマルゾンビ", "5p")
    }
    fun general_merchandiseshop(GUI: Inventory, player: Player) {
        player.openInventory(GUI)
        set_GUIitem(GUI, 0, Material.EMERALD, "${ChatColor.GREEN}10p", "10p")
        set_GUIitem(GUI, 1, Material.EMERALD, "${ChatColor.GREEN}100p", "100p")
        dividing_line(GUI, 9)
        set_GUIitem(GUI, 18, Material.ZOMBIE_HEAD, "${ChatColor.GREEN}敵対されない帽子", "8000p")
        set_GUIitem(GUI, 19, Material.ENCHANTED_GOLDEN_APPLE, "エンチャント金リンゴ", "300")
    }
    fun villagerlevelup(GUI: Inventory, player: Player) {
        player.openInventory(GUI)

        val team_name = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        val level = 6 - Data.DataManager.teamDataMap.getOrPut(team_name) { Team() }.blockTime
        val shop = Data.DataManager.teamDataMap[team_name]?.entities?.lastOrNull()
        set_GUIitem(GUI, 1, Material.RED_DYE, "${ChatColor.YELLOW}★村人体力増加", "${ChatColor.RED}エラー 開き直してください")
        shop?.let { entity ->
            val maxHealthAttribute = shop.getAttribute(Attribute.GENERIC_MAX_HEALTH)
            val maxHealth = maxHealthAttribute?.value?.toInt() ?: 0
            val modifiedMaxHealth = maxHealth / 10 * 50
            set_GUIitem(GUI, 1, Material.RED_DYE, "${ChatColor.YELLOW}★村人体力増加", modifiedMaxHealth.toString() + "p")
        }
        val price = level * 100
        if (level < 5) {
            set_GUIitem(GUI, 0, Material.GOLDEN_PICKAXE, "${ChatColor.YELLOW}★鉱石復活速度UP", price.toString() + "p")
        } else {
            set_GUIitem(GUI, 0, Material.AIR, "", "")
        }
        dividing_line(GUI, 9)
        set_GUIitem(GUI, 18, Material.IRON_BLOCK, "${ChatColor.YELLOW}アイアンゴーレム", "500p")
        set_GUIitem(GUI, 19, Material.GOLD_BLOCK, "${ChatColor.YELLOW}ゴールデンゴーレム", "1500p")
        set_GUIitem(GUI, 20, Material.DIAMOND_BLOCK, "${ChatColor.YELLOW}ダイヤモンドゴーレム", "8000p")
    }
    fun disturbshop(GUI: Inventory) {
        set_GUIitem(GUI, 0, Material.BLACK_CANDLE, "${ChatColor.YELLOW}★盲目(10秒)[妨害]", "300p")
        set_GUIitem(GUI, 1, Material.LIGHT_GRAY_CANDLE, "${ChatColor.YELLOW}★弱体化(10秒)[妨害]", "300p")
        set_GUIitem(GUI, 2, Material.BROWN_CANDLE, "${ChatColor.YELLOW}★採掘速度低下(1分)[妨害]", "300p")
    }

    fun dividing_line(GUI: Inventory, beginning: Int) {
        for (i in beginning..beginning + 8) {
            set_GUIitem(GUI, i, Material.RED_STAINED_GLASS_PANE, "", "")
        }
    }
    fun gamesettingGUI(player: Player) {
        val GUI = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}設定画面")
        set_GUIitem(GUI, 0, Material.EMERALD, "${ChatColor.AQUA}ゲームスタート", "")
        set_GUIitem(GUI, 1, Material.BARRIER, "${ChatColor.RED}終了", "")
        player.openInventory(GUI)
    }
    fun close(title: String, player: Player, inventory: Inventory) {
        if (title == "${ChatColor.DARK_GREEN}チームチェスト") {
            PlayerSend().playsound(player, Sound.BLOCK_CHEST_CLOSE)
            return
        }

        if (title == "${ChatColor.DARK_GREEN}金床") {
            anvil().close(player, inventory)
            return
        }
    }
}
