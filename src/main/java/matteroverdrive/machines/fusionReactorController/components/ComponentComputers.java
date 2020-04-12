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

package matteroverdrive.machines.fusionReactorController.components;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;

/*@Optional.InterfaceList({
		@Optional.Interface(modid = "ComputerCraft", iface = "dan200.computercraft.api.peripheral.IPeripheral")
})*/
public class ComponentComputers extends MachineComponentAbstract<TileEntityMachineFusionReactorController> //implements IPeripheral
{

    private String[] methodNames = new String[]
            {
                    "getStatus",
                    "isValid",
                    "getEnergyGenerated",
                    "getMatterUsed",
                    "getEnergyStored",
                    "getMatterStored"
            };
    private String peripheralName = "mo_fusion_reactor_controller";

    public ComponentComputers(TileEntityMachineFusionReactorController machine) {
        super(machine);
    }


    private Object[] callMethod(int method, Object[] args) {
        switch (method) {
            case 0:
                return computerGetStatus(args);
            case 1:
                return computerIsValid(args);
            case 2:
                return computerGetEnergyGenerated(args);
            case 3:
                return computerGetMatterUsed(args);
            case 4:
                return computerGetEnergyStored(args);
            case 5:
                return computerGetMatterStored(args);
            default:
                throw new IllegalArgumentException("Invalid method id");
        }
    }

    private Object[] computerGetStatus(Object[] args) {
        return new Object[]{machine.getMonitorInfo()};
    }

    private Object[] computerIsValid(Object[] args) {
        return new Object[]{machine.isValidStructure()};
    }

    private Object[] computerGetEnergyGenerated(Object[] args) {
        return new Object[]{machine.getEnergyPerTick()};
    }

    private Object[] computerGetMatterUsed(Object[] args) {
        return new Object[]{machine.getMatterDrainPerTick()};
    }

    private Object[] computerGetEnergyStored(Object[] args) {
        return new Object[]{machine.getEnergyStorage().getEnergyStored()};
    }

    private Object[] computerGetMatterStored(Object[] args) {
        return new Object[]{machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).getMatterStored()};
    }


    /*//region ComputerCraft
	@Override
    @Optional.Method(modid = "ComputerCraft")
    public String getType() {
        return peripheralName;
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public String[] getMethodNames() {
        return methodNames;
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        try {
            return callMethod(method, arguments);
        } catch (Exception e) {
            throw new LuaException(e.getMessage());
        }
    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public void attach(IComputerAccess computer) {

    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public void detach(IComputerAccess computer) {

    }

    @Override
    @Optional.Method(modid = "ComputerCraft")
    public boolean equals(IPeripheral other) {
        return false;
    }



    @Optional.Method(modid = "OpenComputers")
    public String[] methods() {
        return methodNames;
    }

    @Optional.Method(modid = "OpenComputers")
    public Object[] invoke(String method, Context context, Arguments args) throws Exception {
        int methodId = Arrays.asList(methodNames).indexOf(method);

        if (methodId == -1) {
            throw new RuntimeException("The method " + method + " does not exist");
        }

        return callMethod(methodId, args.toArray());
    }

    @Optional.Method(modid = "OpenComputers")
    public String getComponentName() {
        return peripheralName;
    }
*/


    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {

    }

    @Override
    public void registerSlots(Inventory inventory) {

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

    }


}
