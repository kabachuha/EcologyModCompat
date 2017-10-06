package ecomodcompat.compat;

import java.util.List;

import ecomod.api.pollution.PollutionData;
import net.minecraft.tileentity.TileEntity;
import scala.actors.threadpool.Arrays;

public class ImmersiveEngineeringCompat implements IEMCModCompat {

	@Override
	public String getModid()
	{
		return "immersiveengineering";
	}

	@Override
	public List<String> getSupportedTiles()
	{
		return Arrays.asList(new String[]{"immersiveengineering:cokeoven", "immersiveengineering:blastfurnace", "immersiveengineering:blastfurnaceadvanced", "immersiveengineering:alloysmelter", "immersiveengineering:crusher", "immersiveengineering:excavator", "immersiveengineering:assembler", "immersiveengineering:fermenter", "immersiveengineering:refinery", "immersiveengineering:dieselgenerator", "immersiveengineering:arcfurnace"});
	}

	@Override
	public PollutionData processTileEntity(TileEntity tile)
	{
		return PollutionData.getEmpty();
	}

}
