package org.owleebr.professions.NMS;

import com.mojang.math.Transformation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.Display.TextDisplay;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SendPackets {

    public static int showItemDisplay(Player player, Location loc, org.bukkit.inventory.ItemStack stack, boolean fixed, @Nullable Transformation transform) {
        ServerPlayer spMain = ((CraftPlayer) player).getHandle();



        CraftWorld craftWorld = (CraftWorld) loc.getWorld();
        ItemDisplay itemDis = new ItemDisplay(EntityType.ITEM_DISPLAY, craftWorld.getHandle());

        ItemStack item = CraftItemStack.asNMSCopy(stack);
        itemDis.setPos(loc.getX(), loc.getY(), loc.getZ());
        itemDis.setItemStack(item);
        if (!fixed) {
            itemDis.setBillboardConstraints(Display.BillboardConstraints.CENTER);
        }
        if (transform != null) {
            itemDis.setTransformation(transform);
        }

        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(
            itemDis.getId(),
            itemDis.getUUID(),
            itemDis.getX(), itemDis.getY(), itemDis.getZ(),
            itemDis.xRotO, itemDis.yRotO,
            itemDis.getType(),
            0,
            new Vec3(1, 1, 1),
            0
        );
        ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(itemDis.getId(), itemDis.getEntityData().getNonDefaultValues());

        spMain.connection.sendPacket(addEntityPacket);
        spMain.connection.sendPacket(dataPacket);
        return itemDis.getId();

    }

    public static int showTextDisplay(Player player, Location loc, String text, boolean fixed, @Nullable Transformation transform) {
        ServerPlayer spMain = ((CraftPlayer) player).getHandle();



        CraftWorld craftWorld = (CraftWorld) loc.getWorld();
        TextDisplay itemDis = new TextDisplay(EntityType.TEXT_DISPLAY, craftWorld.getHandle());

        itemDis.setPos(loc.getX(), loc.getY(), loc.getZ());
        itemDis.setText(Component.literal(text));
        if (!fixed) {
            itemDis.setBillboardConstraints(Display.BillboardConstraints.CENTER);
        }
        if (transform != null) {
            itemDis.setTransformation(transform);
        }

        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(
            itemDis.getId(),
            itemDis.getUUID(),
            itemDis.getX(), itemDis.getY(), itemDis.getZ(),
            itemDis.xRotO, itemDis.yRotO,
            itemDis.getType(),
            0,
            new Vec3(1, 1, 1),
            0
        );
        ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(itemDis.getId(), itemDis.getEntityData().getNonDefaultValues());

        spMain.connection.sendPacket(addEntityPacket);
        spMain.connection.sendPacket(dataPacket);
        return itemDis.getId();
    }

    public static void stopVision(int EntityID, Player viewer) {
        ClientboundRemoveEntitiesPacket removePacket = new ClientboundRemoveEntitiesPacket(EntityID);
        ServerPlayer spViewer = ((CraftPlayer) viewer).getHandle();
        spViewer.connection.sendPacket(removePacket);
    }

}
