package com.github.Ringoame196

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
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
}
