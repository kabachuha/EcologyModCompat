package ecomodcompat.core;

import java.util.function.Function;

import ecomod.api.pollution.PollutionData;
import ecomodcompat.asm.EMCClassTransformer;
import ecomodcompat.compat.IEMCModCompat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class EMCTEPollutionFunc implements Function<TileEntity, Object[]> {

	@Override
	public Object[] apply(TileEntity arg0)
	{
		PollutionData ret = PollutionData.getEmpty();
		ResourceLocation tek = TileEntity.getKey(arg0.getClass());
		
		if(tek == null || EMCConfig.blacklisted_mods.contains(tek.getResourceDomain()) || EMCConfig.blacklisted_tiles.contains(tek.toString()) || EMCClassTransformer.failed_tiles.contains(tek.toString()))
			return new Object[]{};
		
		boolean force_override = false;
		
		for(IEMCModCompat c : EMCConfig.compats)
		{
			if(c.getSupportedTiles().contains(tek.toString()))
			{
				ret.add(c.processTileEntity(arg0));
				force_override = true;
			}
		}
				
		return new Object[]{ret.getAirPollution(), ret.getWaterPollution(), ret.getSoilPollution(), force_override};
	}

}
