package ecomodcompat.stuff;

import ecomodcompat.core.EcomodCompat;
import net.minecraftforge.common.config.Configuration;

public class CConfig
{
	public static Configuration config;
	
	public static void sync()
	{
		if(config == null)
		{
			EcomodCompat.log.error("The configuration file hasn't been loaded!");
			throw new NullPointerException("The configuration file hasn't been loaded!");
		}
		
		try
		{
			config.load();
			
		}
		catch(Exception e)
		{
			EcomodCompat.log.error("During the EcomodCompat configuration setup the following error had occured:");
			EcomodCompat.log.error(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if(config.hasChanged())
				config.save();
		}
	}
}
