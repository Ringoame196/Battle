package com.github.Ringoame196

import java.util.UUID

class Data {
    object DataManager {
        val teamDataMap: MutableMap<String?, TeamData> = mutableMapOf()
        val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()
        var gameData = Gamedata()
    }
}
