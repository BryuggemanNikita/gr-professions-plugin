package org.owleebr.professions.FoxBlock.Utils;

import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Utils {

    public static final Map<String, Vector> DIR = Map.of(
            "North", new Vector(1, 1, -1),
            "South", new Vector(-1, 1, 1),
            "East",  new Vector(1, 1, 1),
            "West",  new Vector(-1, 1, -1)
    );

    public static final Map<String, Vector> DIR2 = Map.of(
            "North", new Vector(1, 0, 0),
            "South", new Vector(-1, 0, 0),
            "East",  new Vector(0, 0, 1),
            "West",  new Vector(0, 0, -1)
    );

    public static Boolean checkSpace2Block(Block block, String Face){
        Block checkblock = block.getLocation().clone().add(DIR2.get(Face)).getBlock();
        return checkblock.getType() == Material.AIR;
    }

    public static Boolean checkSpace4Block(Block block, String Face){
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 2; j++) {
                Block checkblock = null;
                if (j == 1){
                   checkblock = block.getLocation().clone().add(DIR2.get(Face)).add(0, i, 0).getBlock();
                }else {
                    checkblock = block.getLocation().clone().add(0, i, 0).getBlock();
                }
                if (checkblock.equals(block)){continue;}
                if (checkblock.getType() != Material.AIR){return false;}
            }
        }
        return true;
    }

    public static Boolean checkSpace8Block(Block block, String Face){
        Vector frame = DIR.get(Face);
        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = 0; dz <= 1; dz++) {
                    Block blockc = block.getLocation().clone().add(frame.clone().setX(frame.getX() * dx).setY(frame.getY() * dy).setZ(frame.getZ() * dz)).getBlock();
                    if (blockc.equals(block)) {continue;}
                    if (blockc.getType() != Material.AIR){return false;}
                }
            }
        }
        return true;
    }

    public static Boolean checkSpace12Block(Block block, String Face){
        Vector frame = DIR.get(Face);
        for (int dx = 0; dx <= 1 + Math.abs(DIR2.get(Face).getX()); dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = 0; dz <= 1 + Math.abs(DIR2.get(Face).getX()); dz++) {
                    Block blockc = block.getLocation().clone().add(frame.clone().setX(frame.getX() * dx).setY(frame.getY() * dy).setZ(frame.getZ() * dz)).getBlock();
                    if (blockc.equals(block)) {continue;}
                    if (blockc.getType() != Material.AIR){return false;}
                }
            }
        }
        return true;
    }


}
