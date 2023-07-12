package com.github.Ringoame196

import java.util.UUID

class Data {
    object DataManager {
        val teamDataMap: MutableMap<String?, Team> = mutableMapOf()
        val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()
        val gameData = Gamedata()
    }
}
