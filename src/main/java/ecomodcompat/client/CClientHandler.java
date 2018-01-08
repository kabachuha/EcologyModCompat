package ecomodcompat.client;

import ecomodcompat.core.CConsts;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CClientHandler
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerIcons(TextureStitchEvent.Pre event)
	{
		if(CConsts.HardCompatInfo.weather2_acid_rain_patch)
		{
			CliProxy.W2.radarIconCyclone = event.getMap().registerSprite(CConsts.resloc("weather2/radar/radar_icon_cyclone"));
			CliProxy.W2.radarIconHail = event.getMap().registerSprite(CConsts.resloc("weather2/radar/radar_icon_hail"));
			CliProxy.W2.radarIconLightning = event.getMap().registerSprite(CConsts.resloc("weather2/radar/radar_icon_lightning"));
			CliProxy.W2.radarIconRain = event.getMap().registerSprite(CConsts.resloc("weather2/radar/radar_icon_rain"));
			CliProxy.W2.radarIconTornado = event.getMap().registerSprite(CConsts.resloc("weather2/radar/radar_icon_tornado"));
		}
	}
}
