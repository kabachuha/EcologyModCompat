package ecomodcompat.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

import ecomodcompat.core.CConsts;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

public class EMCSetup implements IFMLCallHook
{
	public File mcLoc = null;
	public File modLoc = null;
	
	@Override
	public Void call() throws Exception
	{
		System.out.println("Setuping EcomodCompat hard properties");
		System.out.println("EcomodCompat file: "+modLoc.getAbsolutePath());
		Properties props = new Properties();
		File hardpropfile = modLoc.isDirectory() ? new File(modLoc.getAbsolutePath() + "/ecomodcompat_setup.properties") : new File(modLoc.getParentFile().getAbsolutePath() + "/ecomodcompat_setup.properties");
		
		if(hardpropfile.exists())
		{
			InputStream input = new FileInputStream(hardpropfile);
			props.load(input);
		}
		
		Class cconsts;
		
		try
		{
			cconsts = Class.forName("ecomodcompat.core.CConsts$HardCompatInfo");
		}
		catch(Exception e)
		{
			EMCTransformer.setupCompleted = true;
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("ecomodcompat.core.CConsts$HardCompatInfo hadn't been found! Hard-defined properties are set to default values!");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return null;
		}
		
		for(Field f : cconsts.getFields())
		{
			if(props.containsKey(f.getName()))
			{
				f.setAccessible(true);
				f.setBoolean(null, "true".equalsIgnoreCase(props.getProperty(f.getName(), Boolean.toString(f.getBoolean(null)))));
			}
			else
			{
				props.setProperty(f.getName(),Boolean.toString(f.getBoolean(null)));
			}
		}
		
		OutputStream output = new FileOutputStream(hardpropfile);
		props.store(output, "Config file for hard-defined ecomodcompat properties, such as ASM transformations");
		
		System.out.println(CConsts.HardCompatInfo.weather2_acid_rain_patch);
		
		EMCTransformer.setupCompleted = true;
		
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		mcLoc = (File) data.get("mcLocation");
		modLoc = (File) data.get("coremodLocation");
	}

}
