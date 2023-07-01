package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
class itemClick {
    val guiclass = GUI()
    fun summonzombie(player: Player, item: ItemStack?) {
        val item_name = item?.itemMeta?.displayName
        if (item_name?.contains("[召喚]") == false) { return }
        val summon_name = item_name?.replace("[召喚]", "")
        if (summon_name == null) { return }
        // ゾンビのカスタム設定
        val location = player.location.subtract(0.0, 2.0, 0.0) // 召喚する位置を指定

        val zombie = player.world.spawnEntity(location, EntityType.ZOMBIE)
        zombie.customName = summon_name // ゾンビの名前
        zombie.isCustomNameVisible = true // ゾンビの名前を表示するかどうか
    }

    fun setting(player: Player) {
        val settingGUI: Inventory = Bukkit.createInventory(null, 9, "${ChatColor.AQUA}攻防戦設定")

        guiclass.set_GUIitem(settingGUI, 0, Material.COBBLESTONE, "${ChatColor.RED}赤 NPC", "未設定")
        guiclass.set_GUIitem(settingGUI, 1, Material.COBBLESTONE, "${ChatColor.BLUE}青 NPC", "未設定")
        guiclass.set_GUIitem(settingGUI, 3, Material.COBBLESTONE, "${ChatColor.RED}赤 ゾンビ", "未設定")
        guiclass.set_GUIitem(settingGUI, 4, Material.COBBLESTONE, "${ChatColor.BLUE}青 ゾンビ", "未設定")
        guiclass.set_GUIitem(settingGUI, 6, Material.COBBLESTONE, "${ChatColor.RED}赤 スポーン", "未設定")
        guiclass.set_GUIitem(settingGUI, 7, Material.COBBLESTONE, "${ChatColor.BLUE}青 スポーン", "未設定")
        guiclass.set_GUIitem(settingGUI, 8, Material.COMMAND_BLOCK, "${ChatColor.GOLD}保存", "")

        player.openInventory(settingGUI)
    }
}
