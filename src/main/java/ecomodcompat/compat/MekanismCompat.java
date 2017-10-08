package ecomodcompat.compat;

import java.util.List;

import ecomod.api.pollution.PollutionData;
import ecomodcompat.core.EMCConfig;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class MekanismCompat implements IEMCModCompat {
	
	public static int gas_mask_gas_usage = 0;

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
	
	public static boolean handle_Mekanism_GasMask_isRespirating(EntityLivingBase entity, ItemStack stack, boolean decr)
	{
		if(entity == null || stack == null)
			return false;
		
		if(entity.world.isRemote)
			return false;
		
		if(entity.hasItemInSlot(EntityEquipmentSlot.CHEST))
		{
			ItemStack item = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			
			if(item.getItem() instanceof IGasItem)
			{
				IGasItem gi = (IGasItem)item.getItem();
				
				GasStack gas_stored = gi.getGas(item); 
				
				if(gas_stored == null || gas_stored.amount == 0)
					return false;
				
				Gas type = GasRegistry.getGas("oxygen");
				
				if(gas_stored.getGas() == type)
				{
					gi.setGas(item, new GasStack(type, Math.max(0, gas_stored.amount - gas_mask_gas_usage)));
					return true;
				}
			}
		}
		
		return false;
	}
}
