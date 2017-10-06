package ecomodcompat.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import ecomodcompat.compat.IEMCModCompat;
import ecomodcompat.compat.ImmersiveEngineeringCompat;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

public class EMCConfig
{
	public static List<String> blacklisted_mods = new ArrayList<String>();
	public static List<String> blacklisted_tiles = new ArrayList<String>();
	public static List<IEMCModCompat> compats = new ArrayList<IEMCModCompat>();
	
	public static void init()
	{
		//TODO Register compats here
		
		addModCompat(new ImmersiveEngineeringCompat());
	}
	
	public static boolean addModCompat(IEMCModCompat compat)
	{
		if(compats.removeIf((c) -> {return c.getModid() == compat.getModid();}))
		{
			compats.add(compat);
			return false;
		}
		
		return compats.add(compat);
	}
	
	public static boolean isCompatActive(IEMCModCompat compat)
	{
		for(IEMCModCompat i : compats)
			return !blacklisted_mods.contains(i.getModid());
		
		return false;
	}
	
	public static void configurate(File config_file)
	{
		Configuration cfg = new Configuration(config_file);
		
		cfg.load();
		
		String[] blackmods = cfg.get(Configuration.CATEGORY_GENERAL, "blacklisted_mods", new String[]{}).getStringList();
		
		String[] blacktiles = cfg.get(Configuration.CATEGORY_GENERAL, "blacklisted_tiles", new String[]{}).getStringList();
		
		for(String s : blackmods)
			blacklisted_mods.add(s);
		
		for(String s : blacktiles)
			blacklisted_tiles.add(s);
	}
}
