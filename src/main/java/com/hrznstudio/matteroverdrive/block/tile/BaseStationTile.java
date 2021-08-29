package com.hrznstudio.matteroverdrive.block.tile;

import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.BasicTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;

public class BaseStationTile<T extends BasicTile<T>> extends BasicTile<T>  {

    public BaseStationTile(BasicTileBlock<T> base) {
        super(base);
    }

    public boolean isUsableByPlayer(PlayerEntity player){
        return true;
    }

}
