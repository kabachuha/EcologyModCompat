package ecomodcompat.asm;

import java.lang.reflect.Field;

import ecomod.api.EcomodAPI;
import ecomod.common.pollution.PollutionEffectsConfig;
import ecomodcompat.client.CliProxy;
import ecomodcompat.core.EcomodCompat;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import weather2.util.CachedNBTTagCompound;
import weather2.weathersystem.storm.StormObject;

public class Weather2Hooks
{
	private static Field is_acidic = null;
	
	private static void setup_is_acidic()
	{
		if(is_acidic == null)
		{
			try
			{
				is_acidic = StormObject.class.getField(EMCTransformer.W2Helper.F_acid);
			}
			catch(Exception ex)
			{
				return;
			}
		}
	}
	
	public static void so_initFirstTime(StormObject so)
	{
		setup_is_acidic();
		
		if(so != null && !so.manager.getWorld().isRemote && is_acidic != null)
			try
			{
				is_acidic.setBoolean(so, PollutionEffectsConfig.isEffectActive("acid_rain", EcomodAPI.getPollution(so.manager.getWorld(), (int)(so.pos.xCoord) >> 4, (int)(so.pos.zCoord) >> 4)));
			}
			catch(Exception ex)
			{
				EcomodCompat.log.error("Weather2: unable to pollute the storm object at pos "+so.pos.toString());
			}
	}
	
	public static void so_nbtSyncForClient(StormObject so, CachedNBTTagCompound data)
	{
		setup_is_acidic();
		
		try
		{
			data.setBoolean("is_acidic", is_acidic.getBoolean(so));
		}
		catch(Exception ex)
		{
			EcomodCompat.log.error("Weather2: unable to store the pollution data of a storm object at pos "+so.pos.toString());
		}
	}
	
	public static void so_nbtSyncFromServer(StormObject so, CachedNBTTagCompound data)
	{
		setup_is_acidic();
		
		try
		{
			is_acidic.setBoolean(so, data.getBoolean("is_acidic"));
		}
		catch(Exception ex)
		{
			EcomodCompat.log.error("Weather2: unable to update the pollution state of a storm object at pos "+so.pos.toString());
		}
	}
	
	public static TextureAtlasSprite fore_getIcon(StormObject storm, TextureAtlasSprite def)
	{
		setup_is_acidic();
		
		boolean flag = false;
		
		try
		{
			flag = is_acidic.getBoolean(storm);
		}
		catch (Exception e)
		{
			//
		}
		
		if(flag)
		{
			String in = def.getIconName();
			if(in.equals("weather2:radar/radar_icon_rain"))
				def = CliProxy.W2.radarIconRain;
			else if(in.equals("weather2:radar/radar_icon_lightning"))
				def = CliProxy.W2.radarIconLightning;
			else if(in.equals("weather2:radar/radar_icon_hail"))
				def = CliProxy.W2.radarIconHail;
			else if(in.equals("weather2:radar/radar_icon_tornado"))
				def = CliProxy.W2.radarIconTornado;
			else if(in.equals("weather2:radar/radar_icon_cyclone"))
				def = CliProxy.W2.radarIconCyclone;
		}
		
		return def;
	}
}
