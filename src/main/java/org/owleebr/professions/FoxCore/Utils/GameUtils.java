package org.owleebr.professions.FoxCore.Utils;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class GameUtils {
    public static void lookAt(Entity entity, Location target) {
        Location loc = entity.getLocation();
        // Вектор от сущности к цели
        Vector direction = target.toVector().subtract(loc.toVector()).normalize();

        // Преобразуем в yaw и pitch
        float yaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
        float pitch = (float) Math.toDegrees(-Math.asin(direction.getY()));

        loc.setYaw(yaw);
        loc.setPitch(pitch);

        // Применяем
        entity.teleport(loc);
    }

    static Vector getForwardVector(String face) {
        switch(face) {
            case "South":  return new Vector(0, 0, -1);
            case "North":  return new Vector(0, 0, 1);
            case "West": return new Vector(1, 0, 0);
            case "East": return new Vector(-1, 0, 0);
        }
        return new Vector(0, 0, 0);
    }

    // Получить вектор right (вправо) для стороны света
    static Vector getRightVector(String dir) {
        switch(dir) {
            case "South": return new Vector(1, 0, 0);
            case "North": return new Vector(-1, 0, 0);
            case "West":   return new Vector(0, 0, 1);
            case "East":   return new Vector(0, 0, -1);
        }
        return new Vector(0, 0, 0);
    }

    public static String getFacingDirection(Entity entity) {
        float yaw = entity.getLocation().getYaw();

        // Приводим угол к диапазону [0, 360)
        yaw = (yaw % 360 + 360) % 360;

        if (yaw >= 45 && yaw < 135) {
            return "West";
        } else if (yaw >= 135 && yaw < 225) {
            return "North";
        } else if (yaw >= 225 && yaw < 315) {
            return "East";
        } else {
            return "South";
        }
    }


}
