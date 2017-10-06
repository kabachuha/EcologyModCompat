package ecomodcompat.compat;

import java.util.List;

import ecomod.api.pollution.PollutionData;
import ecomodcompat.core.EMCConfig;
import net.minecraft.tileentity.TileEntity;

public class MekanismCompat implements IEMCModCompat {

	@Override
	public String getModid() {
		return "mekanism";
	}

	@Override
	public List<String> getSupportedTiles()
	{
		return null;
	}

	@Override
	public PollutionData processTileEntity(TileEntity tile) 
	{
			
		return null;
	}

}
