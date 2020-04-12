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

package matteroverdrive.matter_network.components;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter_network.IMatterNetworkComponent;
import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.transport.MatterNetwork;
import matteroverdrive.machines.IMachineComponent;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public abstract class MatterNetworkComponentClient<T extends MOTileEntityMachine & IMatterNetworkConnection> implements IMachineComponent, IMatterNetworkConnection, ITickable, IMatterNetworkComponent {
    //protected static final PacketHandlerBasicConnections BASIC_CONNECTIONS_HANDLER = new PacketHandlerBasicConnections();
    //protected final List<AbstractMatterNetworkPacketHandler> handlers;
    //protected final MatterNetworkPacketQueue<MatterNetworkPacket> packetQueue;
    protected final T rootClient;
    private MatterNetwork matterNetwork;

    public MatterNetworkComponentClient(T rootClient) {
        this.rootClient = rootClient;
        //packetQueue = new MatterNetworkPacketQueue(rootClient);
        //handlers = new ArrayList<>();
    }

/*    @Override
	public void queuePacket(MatterNetworkPacket packet)
    {
        if (canPreform(packet) && packet.isValid(getworld()))
        {
            getPacketQueue(0).queue(packet);
            packet.tickAlive(getworld(), true);
            packet.onAddedToQueue(getworld(),getPacketQueue(0),0);
        }
    }*/

/*    protected void manageTopPacket()
	{
        for (int i = 0;i < getPacketQueueCount();i++)
        {
            if (getPacketQueue(i).peek() != null)
            {
                try {
                    executePacket(getPacketQueue(i).peek());
                }catch (Exception e)
                {
                    MOLog.log(Level.ERROR,e,"There was a problem while executing packet %s from queue %s",getPacketQueue(i).peek(),i);
                }finally
                {
                    getPacketQueue(i).dequeue();
                    getPacketQueue(i).tickAllAlive(getworld(),true);
                }
            }
        }
    }*/

    /*protected void executePacket(MatterNetworkPacket packet)
    {
        for (AbstractMatterNetworkPacketHandler handler : handlers)
        {
            handler.processPacket(packet,new AbstractMatterNetworkPacketHandler.Context(getworld(),rootClient));
        }
    }

    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        if (phase == TickEvent.Phase.END)
        {
            manageTopPacket();
        }
        return 0;
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        if (packet.getFilter() != null)
        {
            NBTTagList connectionsList = packet.getFilter().getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_LONG);
            for (int i = 0;i < connectionsList.tagCount();i++)
            {
                BlockPos filterPos = BlockPos.fromLong(((NBTTagLong)connectionsList.get(i)).getLong());
                if (filterPos.equals(getNodePos()))
                {
                    return true;
                }
            }
            return false;
        }
        return true;
    }*/

    @Override
    public MatterNetwork getNetwork() {
        return matterNetwork;
    }

    @Override
    public void setNetwork(MatterNetwork network) {
        this.matterNetwork = network;
    }

    @Override
    public World getNodeWorld() {
        return rootClient.getWorld();
    }

    @Override
    public boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction) {
        return canConnectFromSide(blockState, direction);
    }


    @Override
    public BlockPos getNodePos() {
        return rootClient.getPos();
    }

    @Override
    public boolean canConnectFromSide(IBlockState blockState, EnumFacing side) {
        return rootClient.canConnectFromSide(blockState, side);
    }

    @Override
    public boolean establishConnectionFromSide(IBlockState blockState, EnumFacing side) {
        return rootClient.canConnectFromSide(blockState, side);
    }

    @Override
    public void breakConnection(IBlockState blockState, EnumFacing side) {

    }
/*
    @Override
    public MatterNetworkPacketQueue<MatterNetworkPacket> getPacketQueue(int queueID)
    {
        return packetQueue;
    }*/

/*
    @Override
    public int getPacketQueueCount()
    {
        return 1;
    }*/


    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        /*if (categories.contains(MachineNBTCategory.DATA))
        {
            packetQueue.readFromNBT(nbt);
        }*/
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        /*if (categories.contains(MachineNBTCategory.DATA) && toDisk)
        {
            packetQueue.writeToNBT(nbt);
        }*/
    }

    @Override
    public void registerSlots(Inventory inventory) {

    }

    @Override
    public void update() {
        if (!getNodeWorld().isRemote) {
            manageNetwork();
        }
    }

    protected void manageNetwork() {
        if (matterNetwork == null) {
            if (!tryConnectToNeighborNetworks(getNodeWorld())) {
                MatterNetwork network = MatterOverdrive.MATTER_NETWORK_HANDLER.getNetwork(rootClient);
                network.addNode(rootClient);
            }
        }
    }

    protected boolean tryConnectToNeighborNetworks(World world) {
        boolean hasConnected = false;
        for (EnumFacing side : EnumFacing.VALUES) {
            if (rootClient.canConnectFromSide(world.getBlockState(getNodePos()), side)) {
                TileEntity neighborEntity = world.getTileEntity(getNodePos().offset(side));
                if (neighborEntity instanceof IMatterNetworkConnection && canConnectToNetworkNode(world.getBlockState(getNodePos()), (IMatterNetworkConnection) neighborEntity, side)) {
                    if (((IMatterNetworkConnection) neighborEntity).getNetwork() != null && ((IMatterNetworkConnection) neighborEntity).getNetwork() != this.matterNetwork) {
                        ((IMatterNetworkConnection) neighborEntity).getNetwork().addNode(rootClient);
                        hasConnected = true;
                    }
                }
            }
        }
        return hasConnected;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onMachineEvent(MachineEvent event) {
        if (event instanceof MachineEvent.Destroyed) {
            onDestroyed((MachineEvent.Destroyed) event);
        } else if (event instanceof MachineEvent.Added) {
            onAdded((MachineEvent.Added) event);
        } else if (event instanceof MachineEvent.Unload) {
            onUnload((MachineEvent.Unload) event);
        }
    }

    protected void onDestroyed(MachineEvent.Destroyed event) {
        if (!event.world.isRemote) {
            if (matterNetwork != null) {
                matterNetwork.onNodeDestroy(event.state, rootClient);
            }
            for (EnumFacing enumFacing : EnumFacing.VALUES) {
                if (canConnectFromSide(event.state, enumFacing)) {
                    TileEntity tileEntityNeignbor = event.world.getTileEntity(event.pos.offset(enumFacing));
                    IBlockState neighborState = event.world.getBlockState(event.pos.offset(enumFacing));
                    if (tileEntityNeignbor instanceof IMatterNetworkConnection) {
                        ((IMatterNetworkConnection) tileEntityNeignbor).breakConnection(neighborState, enumFacing.getOpposite());
                    }
                }
            }
        }
    }

    protected void onAdded(MachineEvent.Added event) {
        if (!event.world.isRemote) {
            for (EnumFacing enumFacing : EnumFacing.VALUES) {
                TileEntity tileEntityNeignbor = event.world.getTileEntity(event.pos.offset(enumFacing));
                IBlockState neighborState = event.world.getBlockState(event.pos.offset(enumFacing));
                if (canConnectFromSide(event.state, enumFacing) && tileEntityNeignbor instanceof IMatterNetworkConnection) {
                    if (((IMatterNetworkConnection) tileEntityNeignbor).establishConnectionFromSide(neighborState, enumFacing.getOpposite())) {

                    }
                }
            }
        }
    }

    protected void onUnload(MachineEvent.Unload event) {
        if (!getNodeWorld().isRemote) {
            if (!getNodeWorld().isRemote) {
                IBlockState blockState = getNodeWorld().getBlockState(getNodePos());
                if (matterNetwork != null) {
                    matterNetwork.onNodeDestroy(blockState, rootClient);
                }
            }
        }
    }

}
