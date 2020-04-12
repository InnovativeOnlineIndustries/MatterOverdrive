/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.tile.pipes;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.data.transport.MatterNetwork;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityNetworkPipe extends TileEntityPipe implements IMatterNetworkConnection {

    private MatterNetwork matterNetwork;

    public TileEntityNetworkPipe() {

    }

    @Override
    public void update() {
        if (!world.isRemote) {
            manageNetwork();
        }
    }

    public void manageNetwork() {
        if (matterNetwork == null) {
            if (!tryConnectToNeighborNetworks(world)) {
                MatterNetwork network = MatterOverdrive.MATTER_NETWORK_HANDLER.getNetwork(this);
                network.addNode(this);
            }
        }
    }

    public boolean tryConnectToNeighborNetworks(World world) {
        boolean hasConnected = false;
        for (EnumFacing side : EnumFacing.VALUES) {
            BlockPos neighborPos = pos.offset(side);
            if (world.isBlockLoaded(neighborPos)) {
                TileEntity neighborEntity = world.getTileEntity(neighborPos);
                if (neighborEntity instanceof IMatterNetworkConnection && isConnectableSide(side)) {
                    if (((IMatterNetworkConnection) neighborEntity).getNetwork() != null && ((IMatterNetworkConnection) neighborEntity).getNetwork() != this.matterNetwork) {
                        ((IMatterNetworkConnection) neighborEntity).getNetwork().addNode(this);
                        hasConnected = true;
                    }
                }
            }
        }
        return hasConnected;
    }

    @Override
    public boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction) {
        return isConnectableSide(direction);
    }

    @Override
    public boolean canConnectToPipe(TileEntity entity, EnumFacing direction) {
        return isConnectableSide(direction);
    }

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            int connectionCount = 0;
            for (EnumFacing enumFacing : EnumFacing.VALUES) {
                BlockPos neighborPos = pos.offset(enumFacing);
                TileEntity tileEntityNeignbor = world.getTileEntity(neighborPos);
                IBlockState neighborState = world.getBlockState(neighborPos);
                if (tileEntityNeignbor instanceof IMatterNetworkConnection) {
                    if (connectionCount < 2 && ((IMatterNetworkConnection) tileEntityNeignbor).establishConnectionFromSide(neighborState, enumFacing.getOpposite())) {
                        this.setConnection(enumFacing, true);
                        world.markBlockRangeForRenderUpdate(pos, pos);
                        connectionCount++;
                    }
                }
            }
        }
    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            if (matterNetwork != null) {
                matterNetwork.onNodeDestroy(state, this);
            }
            for (EnumFacing enumFacing : EnumFacing.VALUES) {
                if (isConnectableSide(enumFacing)) {
                    TileEntity tileEntityConnection = worldIn.getTileEntity(pos.offset(enumFacing));
                    if (tileEntityConnection instanceof IMatterNetworkConnection) {
                        ((IMatterNetworkConnection) tileEntityConnection).breakConnection(state, enumFacing.getOpposite());
                    }
                }
            }
        }
    }

    @Override
    public void onChunkUnload() {
        if (!world.isRemote) {
            IBlockState blockState = world.getBlockState(getPos());
            if (matterNetwork != null) {
                matterNetwork.onNodeDestroy(blockState, this);
                //MOLog.info("Chunk Unload");
            }
        }
    }

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

/*    @Override
	public boolean isValid() {
        return true;
    }

    @Override
    public void broadcast(MatterNetworkPacket packet,EnumFacing direction)
    {
        if (isValid())
        {
            for (int i = 0; i < 6; i++)
            {
                if (direction.getOpposite().ordinal() != i)
                    MatterNetworkHelper.broadcastPacketInDirection(world, packet, this, EnumFacing.VALUES[i]);
            }
        }
    }*/

    @Override
    public boolean canConnectFromSide(IBlockState blockState, EnumFacing side) {
        return MOMathHelper.getBoolean(getConnectionsMask(), side.ordinal());
    }

    @Override
    public BlockPos getNodePos() {
        return getPos();
    }

    @Override
    public World getNodeWorld() {
        return getWorld();
    }

    @Override
    public boolean establishConnectionFromSide(IBlockState blockState, EnumFacing side) {
        int connCount = getConnectionsCount();
        if (connCount < 2) {
            if (!MOMathHelper.getBoolean(getConnectionsMask(), side.ordinal())) {
                setConnection(side, true);
                world.markBlockRangeForRenderUpdate(pos, pos);
                return true;
            }
        }
        return false;
    }

    @Override
    public void breakConnection(IBlockState blockState, EnumFacing side) {
        setConnection(side, false);
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public void updateSides(boolean notify) {

    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public MatterNetwork getNetwork() {
        return matterNetwork;
    }

    @Override
    public void setNetwork(MatterNetwork network) {
        matterNetwork = network;
    }
}
