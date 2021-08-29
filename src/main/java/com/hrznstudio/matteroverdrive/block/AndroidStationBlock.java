package com.hrznstudio.matteroverdrive.block;

import com.hrznstudio.matteroverdrive.block.tile.AndroidStationTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class AndroidStationBlock extends BasicTileBlock<AndroidStationTile> {

    public AndroidStationBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), AndroidStationTile.class);
    }

    @Override
    public IFactory<AndroidStationTile> getTileEntityFactory() {
        return AndroidStationTile::new;
    }

    @Override
    public Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) Item.getItemFromBlock(this));
        return super.asItem();
    }

    @Override
    public TileEntityType getTileEntityType() {
        return MOBlocks.ANDROID_STATION_TILE.get();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        TileUtil.getTileEntity(worldIn, pos, AndroidStationTile.class).ifPresent(androidStationTile -> androidStationTile.openGui(player));
        return ActionResultType.SUCCESS;
    }
}
