package org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.Bellows;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.owleebr.professions.FoxBlock.Const.Keys;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Annotations.ABlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.FuncBlock;
import org.owleebr.professions.FoxFunctionalBlock.FunctionalBlocks.Blocks.SmitingFurnace.SmitingFurnace;
import org.owleebr.professions.Main;

@ABlock(name = "Bellows")
public class Bellows extends FuncBlock {
    Location BlockLocation;

    int MaxtTemp = Main.getInstance().getConfig().getInt("maxTemp"); //Максимальная температура

    public Bellows(Location location) {
        super(location);
        BlockLocation = location;
    }

    @Override
    public void onClick(Player player, Action action) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location loc = BlockLocation.clone().add(x, 0, z);
                Block block = loc.getBlock();
                CustomBlockData data = new CustomBlockData(block, Main.getInstance());
                if (data.has(Keys.MainBlock)){
                    Location mainBlock = data.get(Keys.MainBlock, DataType.LOCATION);
                    if (Main.blockManager.blocks.containsKey(mainBlock)) {
                        if (Main.blockManager.blocks.get(mainBlock) instanceof SmitingFurnace furnace) {
                            furnace.setTemp(Math.min(furnace.getTemp() + 1, MaxtTemp));
                            break;
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onBlockPlace(Block block, Player player) {

    }
}
