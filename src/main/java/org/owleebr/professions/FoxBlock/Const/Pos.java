package org.owleebr.professions.FoxBlock.Const;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joml.Quaternionf;

public class Pos {
    public static final Map<String, List<Quaternionf>> Rotations = new HashMap<>();
    static {
        Rotations.put("North", List.of(
                new Quaternionf(),                            // no rotation
                new Quaternionf()
        ));
        Rotations.put("South", List.of(
                new Quaternionf().rotateY((float) Math.toRadians(180)), // поворот на 180°
                new Quaternionf()
        ));
        Rotations.put("East", List.of(
                new Quaternionf().rotateY((float) Math.toRadians(-90)),
                new Quaternionf()
        ));
        Rotations.put("West", List.of(
                new Quaternionf().rotateY((float) Math.toRadians(90)),
                new Quaternionf()
        ));
    }
}
