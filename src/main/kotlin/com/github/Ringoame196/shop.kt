package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent

class shop {
    fun open(e: PlayerInteractEntityEvent, player: Player, entity: Mob, team: String) {
        e.isCancelled = true
        Events.DataManager.teamDataMap[team]?.entities?.add(entity)
        GUI(player)
    }
    fun GUI(player: Player) {
        val GUI = Bukkit.createInventory(null, 27, ChatColor.BLUE.toString() + "攻防戦ショップ")
        val point = Events.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        GUI().set_GUIitem(GUI, 0, Material.EMERALD, "${ChatColor.GREEN}所持ポイント:" + point + "p", "")
        GUI().set_GUIitem(GUI, 1, Material.IRON_PICKAXE, "${ChatColor.YELLOW}ピッケル", "")
        GUI().set_GUIitem(GUI, 3, Material.IRON_SWORD, "${ChatColor.YELLOW}武器", "")
        GUI().set_GUIitem(GUI, 5, Material.IRON_CHESTPLATE, "${ChatColor.YELLOW}防具", "")
        GUI().set_GUIitem(GUI, 7, Material.TNT, "${ChatColor.YELLOW}お邪魔アイテム", "")
        GUI().set_GUIitem(GUI, 9, Material.ANVIL, "${ChatColor.YELLOW}金床", "エンチャント用")
        GUI().set_GUIitem(GUI, 10, Material.POTION, "${ChatColor.YELLOW}チーム強化", "")
        GUI().set_GUIitem(GUI, 12, Material.VILLAGER_SPAWN_EGG, "${ChatColor.YELLOW}村人強化", "")
        GUI().set_GUIitem(GUI, 14, Material.ZOMBIE_SPAWN_EGG, "${ChatColor.YELLOW}ゾンビ", "")
        GUI().set_GUIitem(GUI, 16, Material.BEACON, "${ChatColor.YELLOW}その他", "")
        GUI().set_GUIitem(GUI, 18, Material.CHEST, "${ChatColor.YELLOW}共通チェスト", "チーム共通")
        GUI().no_set(GUI, 19)
        GUI().no_set(GUI, 21)
        GUI().no_set(GUI, 23)
        GUI().no_set(GUI, 25)

        player.openInventory(GUI)
    }
}
