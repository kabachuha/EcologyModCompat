package ecomodcompat.compat;

import java.util.List;

import ecomod.api.pollution.PollutionData;
import ecomodcompat.core.EMCConfig;
import net.minecraft.tileentity.TileEntity;

public interface IEMCModCompat 
{
	public String getModid();
	
	public List<String> getSupportedTiles();
	
	public PollutionData processTileEntity(TileEntity tile);
}
