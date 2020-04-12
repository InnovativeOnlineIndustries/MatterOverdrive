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

package matteroverdrive.data;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.tile.MOTileEntityMachineMatter;

public class MachineMatterStorage<T extends MOTileEntityMachineMatter> extends MatterStorage {
    protected final T machine;

    public MachineMatterStorage(T machine, int capacity) {
        this(machine, capacity, capacity, capacity);
    }

    public MachineMatterStorage(T machine, int capacity, int maxExtract) {
        this(machine, capacity, maxExtract, maxExtract);
    }

    public MachineMatterStorage(T machine, int capacity, int maxExtract, int maxReceive) {
        super(capacity, maxExtract, maxReceive);
        this.machine = machine;
    }

    @Override
    public int getCapacity() {
        return Math.max(0, (int) (super.getCapacity() * machine.getUpgradeMultiply(UpgradeTypes.MatterStorage)));
    }

    @Override
    public int getMaxExtract() {
        return Math.max(0, (int) (super.getMaxExtract() * machine.getUpgradeMultiply(UpgradeTypes.MatterTransfer)));
    }

    @Override
    public int getMaxReceive() {
        return Math.max(0, (int) (super.getMaxReceive() * machine.getUpgradeMultiply(UpgradeTypes.MatterTransfer)));
    }

    @Override
    public int extractMatter(int amount, boolean simulate) {
        int extracted = super.extractMatter(amount, simulate);
        if (!simulate && extracted != 0) {
            machine.updateClientMatter();
        }
        return extracted;
    }

    @Override
    public int receiveMatter(int amount, boolean simulate) {
        int received = super.receiveMatter(amount, simulate);
        if (!simulate && received != 0) {
            machine.updateClientMatter();
        }
        return received;
    }

    @Override
    public void setMatterStored(int amount) {
        int lastMatter = super.getMatterStored();
        super.setMatterStored(amount);
        if (lastMatter != amount) {
            machine.updateClientMatter();
        }
    }

    @Override
    public int modifyMatterStored(int amount) {
        int modifiedAmount = super.modifyMatterStored(amount);
        if (modifiedAmount != 0) {
            machine.updateClientMatter();
        }
        return modifiedAmount;
    }
}
