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

package appeng.tile.powersink;


import net.darkhax.tesla.api.ITeslaConsumer;

import appeng.api.config.PowerUnits;


/**
 * Adapts an {@link IExternalPowerSink} to Forges {@link net.darkhax.tesla.api.ITeslaConsumer}.
 */
class TeslaEnergyAdapter implements ITeslaConsumer
{

	private final IExternalPowerSink sink;

	TeslaEnergyAdapter( IExternalPowerSink sink )
	{
		this.sink = sink;
	}

	@Override
	public long givePower( long power, boolean simulated )
	{
		// Cut it down to what we can represent in a double
		double powerDbl = (double) power;

		double networkDemand = this.sink.getExternalPowerDemand( PowerUnits.RF, powerDbl );
		long used = (long) Math.min( powerDbl, networkDemand );

		if( !simulated )
		{
			this.sink.injectExternalPower( PowerUnits.RF, used );
		}

		return used;
	}

}
