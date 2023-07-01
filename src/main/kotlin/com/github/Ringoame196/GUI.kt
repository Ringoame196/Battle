package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class GUI {
    fun set_GUIitem(GUI: Inventory, number: Int, set_item: Material, displayname: String, lore: String) {
        // GUIにアイテムを楽にセットする
        var item = ItemStack(set_item)
        var itemMeta: ItemMeta? = item.itemMeta
        itemMeta?.setDisplayName(displayname)
        val lore_list: MutableList<String> = mutableListOf()
        lore_list.add(lore)
        itemMeta?.lore = lore_list
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
        set_GUIitem(GUI, 7, Material.ENCHANTED_BOOK, "${ChatColor.YELLOW}エンチャント", "")
        set_GUIitem(GUI, 10, Material.POTION, "${ChatColor.YELLOW}ポーション", "")
        set_GUIitem(GUI, 12, Material.REINFORCED_DEEPSLATE, "${ChatColor.YELLOW}強化", "")
        set_GUIitem(GUI, 14, Material.ZOMBIE_SPAWN_EGG, "${ChatColor.YELLOW}ゾンビ", "")
        set_GUIitem(GUI, 16, Material.BEACON, "${ChatColor.YELLOW}その他", "")
        set_GUIitem(GUI, 18, Material.CHEST, "${ChatColor.YELLOW}共通チェスト", "")

        no_set(GUI, 19)
        no_set(GUI, 21)
        no_set(GUI, 23)
        no_set(GUI, 25)
    }
}
