package com.github.Ringoame196

import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager

class Zombie {
    fun getNearestVillager(location: org.bukkit.Location, radius: Double): Villager? {
        var nearestVillager: Villager? = null
        var nearestDistanceSquared = Double.MAX_VALUE

        for (entity in location.world!!.getNearbyEntities(location, radius, radius, radius)) {
            if (entity.type == EntityType.VILLAGER && entity is Villager) {
                val villager = entity
                val distanceSquared = villager.location.distanceSquared(location)

                if (distanceSquared < nearestDistanceSquared) {
                    nearestDistanceSquared = distanceSquared
                    nearestVillager = villager
                }
            }
        }

        return nearestVillager
    }
}
