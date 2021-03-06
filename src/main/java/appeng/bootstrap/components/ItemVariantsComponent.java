/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.bootstrap.components;


import java.util.Collection;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

import appeng.bootstrap.IBootstrapComponent;


public class ItemVariantsComponent implements IBootstrapComponent
{

	private final Item item;

	private final Collection<ResourceLocation> resources;

	public ItemVariantsComponent( Item item, Collection<ResourceLocation> resources )
	{
		this.item = item;
		this.resources = resources;
	}

	@Override
	public void preInitialize( Side side )
	{
		ResourceLocation[] resourceArr = this.resources.toArray( new ResourceLocation[0] );
		ModelBakery.registerItemVariants( this.item, resourceArr );
	}
}
