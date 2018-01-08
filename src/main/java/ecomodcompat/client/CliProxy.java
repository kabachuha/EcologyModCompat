package ecomodcompat.client;

import ecomodcompat.common.CoProxy;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.common.MinecraftForge;

public class CliProxy extends CoProxy 
{
	//Data
	public static class W2
	{
		public static TextureAtlasSprite radarIconRain;
		public static TextureAtlasSprite radarIconLightning;
		public static TextureAtlasSprite radarIconHail;
		public static TextureAtlasSprite radarIconTornado;
		public static TextureAtlasSprite radarIconCyclone;
	}
	
	@Override
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new CClientHandler());
	}
	
	@Override
	public void init()
	{
		
	}
	
	@Override
	public void postInit()
	{
		
	}
}
