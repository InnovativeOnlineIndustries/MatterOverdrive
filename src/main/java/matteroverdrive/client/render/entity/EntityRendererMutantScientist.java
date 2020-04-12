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

package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import matteroverdrive.client.model.ModelHulkingScientist;
import matteroverdrive.entity.monster.EntityMutantScientist;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class EntityRendererMutantScientist extends RenderBiped<EntityMutantScientist> {

    private final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "hulking_scinetist.png");

    public EntityRendererMutantScientist(RenderManager renderManager) {
        super(renderManager, new ModelHulkingScientist(), 0);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMutantScientist entity) {
        return texture;
    }

}
