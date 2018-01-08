package ecomodcompat.core;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class CConsts
{
	public static final String modid = "ecomodcompat";
	
	public static final String name = "Ecology Mod|Compat";
	
	/**	
	 * MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH <br> 
	 * 
	 * 	https://mcforge.readthedocs.io/en/latest/conventions/versioning/
	 * 
	 */
	public static final String mcversion = "1.12.2";
	public static final String version = mcversion + "-1.0.0.0-beta";
	
	public static final String deps = "required-after:ecomod@[1.3,)";
	
	public static final String json = "";
	
	//
	
	public static final String githubURL = "https://github.com/Artem226/EcologyModCompat";
	
	public static final String issues = "https://github.com/Artem226/EcologyModCompat/issues";
	
	public static final String projectURL = "https://minecraft.curseforge.com/projects/ecology-mod";//FIXME
	
	
	public static final String contributors = "Artem226(author/maintainer) and all feedbackers.";
	
	public static final List<String> authors = Arrays.asList(new String[]{"Artem226"});
	
	//Proxies
	
	public static final String common_proxy = modid+".common.CoProxy";
	public static final String client_proxy = modid+".client.CliProxy";
	
	// Consts and global variables
	
	public static boolean asm_transformer_inited = false;
	
	public static class HardCompatInfo
	{
		public static boolean weather2_acid_rain_patch = true;
	}
	
	public static ResourceLocation resloc(String path)
	{
		return new ResourceLocation(modid, path);
	}
}
