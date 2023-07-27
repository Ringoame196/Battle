package com.github.Ringoame196

import org.bukkit.entity.Entity

class inspection {
    fun shop(entity: Entity): Boolean {
        var affiliation = false
        if (entity.scoreboardTags.contains("shop")) {
            affiliation = true
        }
        return affiliation
    }
}
