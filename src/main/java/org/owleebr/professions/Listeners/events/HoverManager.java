package org.owleebr.professions.Listeners.events;

import io.papermc.paper.configuration.type.fallback.FallbackValue.Int;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.owleebr.professions.CustomEvents.PlayerHoverBlockEvent;
import org.owleebr.professions.CustomEvents.PlayerStopHoverBlockEvent;
import org.owleebr.professions.Main;
import org.owleebr.professions.NMS.SendPackets;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoverManager {
    private static  final Map<Player, Block> lastBlock = new HashMap<>();
    public static void registEvent(){
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Block current = player.getTargetBlockExact(5); // дистанция
                Block previous = lastBlock.get(player);

                if (current != null && !current.equals(previous)) {
                    // навёлся на новый блок
                    if (previous != null) {
                        Bukkit.getPluginManager().callEvent(new PlayerStopHoverBlockEvent(player, previous));
                    }
                    Bukkit.getPluginManager().callEvent(new PlayerHoverBlockEvent(player, current));
                    lastBlock.put(player, current);
                } else if (current == null && previous != null) {
                    // перестал смотреть на блок
                    Bukkit.getPluginManager().callEvent(new PlayerStopHoverBlockEvent(player, previous));
                    lastBlock.remove(player);
                }
            }
        }, 0L, 2L);

    }
}
