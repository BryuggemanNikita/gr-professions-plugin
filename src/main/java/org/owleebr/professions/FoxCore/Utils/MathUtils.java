package org.owleebr.professions.FoxCore.Utils;

import com.sun.jna.platform.unix.solaris.LibKstat.KstatNamed.UNION.STR;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;

import static org.owleebr.professions.FoxCore.Utils.GameUtils.getForwardVector;
import static org.owleebr.professions.FoxCore.Utils.GameUtils.getRightVector;

public class MathUtils {
    public static Vector rotateAroundY(Vector v, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double x = v.getX();
        double z = v.getZ();

        return new Vector(
                x * cos + z * sin,
                v.getY(),
                x * sin + z * cos
        );
    }

    public static Vector moveVector(Vector original, float forwardMove, float rightMove, String dir) {
        Vector forward = getForwardVector(dir);
        Vector right = getRightVector(dir);

        // Считаем смещение
        Vector offset = forward.multiply(forwardMove).add(right.multiply(rightMove));

        // Возвращаем новую позицию
        return original.clone().add(offset);
    }

    public static Quaternionf rotateWithQuaternion(String face) {
        Quaternionf rotation = new Quaternionf();

        switch (face) {
            case "South" -> rotation.rotateY((float) Math.toRadians(180));
            case "North" -> rotation.rotateY(0);
            case "West"  -> rotation.rotateY((float) Math.toRadians(90));
            case "East"  -> rotation.rotateY((float) Math.toRadians(-90));
        }

        return rotation;
    }

    public static float getDegrees(String face) {
        if (face == "West") {
            return -90;
        }else if (face == "East") {
            return 90;
        }else if (face == "North") {
            return 180;
        }else if (face == "South") {
            return 0;
        }
        return 0;
    }

    public static float getDegreesForVec(String face) {
        if (face == "West") {
            return 90;
        }else if (face == "East") {
            return -90;
        }else if (face == "South") {
            return 0;
        }else if (face == "North") {
            return 180;
        }
        return 0;
    }

}
