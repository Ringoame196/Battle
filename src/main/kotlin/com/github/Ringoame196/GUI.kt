package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
class GUI {
    fun set_GUIitem(GUI: Inventory, number: Int, set_item: Material, displayname: String, lore: String) {
        // GUIにアイテムを楽にセットする
        val item = ItemStack(set_item)
        val itemMeta: ItemMeta? = item.itemMeta
        itemMeta?.setDisplayName(displayname)
        val lore_list: MutableList<String> = mutableListOf()
        lore_list.add(lore)
        itemMeta?.lore = lore_list
        itemMeta?.isUnbreakable = true // 不破壊
        item.setItemMeta(itemMeta)
        GUI.setItem(number, item)
    }
    fun no_set(GUI: Inventory, number: Int) {
        set_GUIitem(GUI, number, Material.OAK_SIGN, "${ChatColor.YELLOW}近日公開", "")
    }

    fun home(GUI: Inventory, point: Int) {
        set_GUIitem(GUI, 0, Material.EMERALD, "${ChatColor.GREEN}所持ポイント:" + point + "p", "")
        set_GUIitem(GUI, 1, Material.IRON_PICKAXE, "${ChatColor.YELLOW}ピッケル", "")
        set_GUIitem(GUI, 3, Material.IRON_SWORD, "${ChatColor.YELLOW}武器", "")
        set_GUIitem(GUI, 5, Material.IRON_CHESTPLATE, "${ChatColor.YELLOW}防具", "")
        set_GUIitem(GUI, 9, Material.ANVIL, "${ChatColor.YELLOW}金床", "エンチャント用")
        set_GUIitem(GUI, 10, Material.POTION, "${ChatColor.YELLOW}チーム強化", "")
        set_GUIitem(GUI, 12, Material.VILLAGER_SPAWN_EGG, "${ChatColor.YELLOW}村人強化", "")
        set_GUIitem(GUI, 14, Material.ZOMBIE_SPAWN_EGG, "${ChatColor.YELLOW}ゾンビ", "")
        set_GUIitem(GUI, 16, Material.BEACON, "${ChatColor.YELLOW}その他", "")
        set_GUIitem(GUI, 18, Material.CHEST, "${ChatColor.YELLOW}共通チェスト", "チーム共通")

        no_set(GUI, 7)
        no_set(GUI, 19)
        no_set(GUI, 21)
        no_set(GUI, 23)
        no_set(GUI, 25)
    }
    fun pickaxeshop(GUI: Inventory, player: Player) {
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
        player.openInventory(GUI)
    }
    fun weaponshop(GUI: Inventory, player: Player) {
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
        player.openInventory(GUI)
    }
    fun equipmentshop(GUI: Inventory, player: Player) {
        dividing_line(GUI, 18)
        set_GUIitem(GUI, 0, Material.GOLDEN_CHESTPLATE, "金のチェストプレート", "100p")
        set_GUIitem(GUI, 1, Material.GOLDEN_LEGGINGS, "金のレギンス", "100p")
        set_GUIitem(GUI, 2, Material.GOLDEN_BOOTS, "金のブーツ", "100p")
        set_GUIitem(GUI, 4, Material.DIAMOND_CHESTPLATE, "ダイヤモンドのチェストプレート", "2500p")
        set_GUIitem(GUI, 5, Material.DIAMOND_LEGGINGS, "ダイヤモンドのレギンス", "2500p")
        set_GUIitem(GUI, 6, Material.DIAMOND_BOOTS, "ダイヤモンドのブーツ", "2500p")
        set_GUIitem(GUI, 9, Material.IRON_CHESTPLATE, "鉄のチェストプレート", "300p")
        set_GUIitem(GUI, 10, Material.IRON_LEGGINGS, "鉄のレギンス", "300p")
        set_GUIitem(GUI, 11, Material.IRON_BOOTS, "鉄のブーツ", "300p")
        set_GUIitem(GUI, 13, Material.NETHERITE_CHESTPLATE, "ネザーライトのチェストプレート", "5000p")
        set_GUIitem(GUI, 14, Material.NETHERITE_LEGGINGS, "ネザーライトのレギンス", "5000p")
        set_GUIitem(GUI, 15, Material.NETHERITE_BOOTS, "ネザーライトのブーツ", "5000p")
        set_enchant_GUIitem(GUI, 27, "20p", Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        set_enchant_GUIitem(GUI, 28, "100p", Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        set_enchant_GUIitem(GUI, 29, "300p", Enchantment.PROTECTION_ENVIRONMENTAL, 3)
        set_enchant_GUIitem(GUI, 30, "500p", Enchantment.PROTECTION_ENVIRONMENTAL, 4)
        player.openInventory(GUI)
    }
    fun enchant_anvil(player: Player) {
        val anvil: Inventory = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}金床")
        for (i in 0..7) {
            set_GUIitem(anvil, i, Material.RED_STAINED_GLASS_PANE, " ", "")
        }
        set_GUIitem(anvil, 3, Material.AIR, "", "")
        set_GUIitem(anvil, 5, Material.AIR, "", "")

        set_GUIitem(anvil, 8, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}合成", "")
        player.openInventory(anvil)
    }
    fun set_enchant_GUIitem(GUI: Inventory, number: Int, lore: String, enchant: Enchantment, level: Int) {
        // GUIにアイテムを楽にセットする
        val item = ItemStack(Material.ENCHANTED_BOOK)
        val itemMeta: ItemMeta? = item.itemMeta
        if (itemMeta is EnchantmentStorageMeta) {
            itemMeta.addStoredEnchant(enchant, level, true)
        }
        val loreList: MutableList<String> = mutableListOf()
        loreList.add(lore)
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        GUI.setItem(number, item)
    }
    fun dividing_line(GUI: Inventory, beginning: Int) {
        for (i in beginning..beginning + 8) {
            set_GUIitem(GUI, i, Material.RED_STAINED_GLASS_PANE, "", "")
        }
    }
}
