package ecomodcompat.asm;

import ecomod.api.EcomodAPI;
import ecomod.api.EcomodStuff;
import ecomod.api.pollution.PollutionData;
import ecomodcompat.core.EMCUtils;
import ecomodcompat.core.EcomodCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fluids.FluidTank;

public class EMCASMHooks
{
	public static void handle_IE_TEMulti_Process_doProcessTick(Object o, int ticksAdded)
	{
		if(ticksAdded > 0 && o instanceof TileEntity)
		{
			TileEntity te = ((TileEntity)o);
			
			PollutionData tile_pollution = EcomodStuff.tile_entity_pollution.getTilePollution(TileEntity.getKey(te.getClass()));
					
			if(tile_pollution != null && !tile_pollution.isEmpty())
			{
				EcomodAPI.emitPollution(te.getWorld(), EMCUtils.blockPosToPair(te.getPos()), tile_pollution.multiplyAll(ticksAdded / 1200F), true);
			}
		}
		else
		{
			EcomodCompat.log.error("An object supposed to extend TileEntity, do is not an instance of TileEntity!");
		}
	}
	
	public static void handle_IE_Excavator_digBlock(Object o)
	{
		handle_IE_TEMulti_Process_doProcessTick(o, 10);
	}
	
	public static void handle_IE_TileEntity_Tick(Object o)
	{
		handle_IE_TEMulti_Process_doProcessTick(o, 1);
	}
	
	public static void handle_IE_TileEntity_Tick_WithCondition(Object o, boolean condition)
	{
		if(condition)
			handle_IE_TileEntity_Tick(o);
	}
	
	public static void handle_IE_Assembler_doProcessOutput(Object o, ItemStack is)
	{
		if(o instanceof TileEntity && is != null && !is.isEmpty())
		{
			TileEntity te = ((TileEntity)o);
			
			PollutionData tile_pollution = EcomodStuff.tile_entity_pollution.getTilePollution(TileEntity.getKey(te.getClass()));
					
			if(tile_pollution != null && !tile_pollution.isEmpty())
			{
				EcomodAPI.emitPollution(te.getWorld(), EMCUtils.blockPosToPair(te.getPos()), tile_pollution.multiplyAll(is.getCount() / 64F), true);
			}
		}
		else
		{
			EcomodCompat.log.error("An object supposed to extend TileEntity, do is not an instance of TileEntity!");
		}
	}
}
