package ecomodcompat.asm;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class EMCLoadingPlugin implements IFMLLoadingPlugin {

	static File mod_location = null;
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{"ecomodcompat.asm.EMCClassTransformer"};
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return EMCLoadingPlugin.Setup.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		if(data.containsKey("coremodLocation"))
		{
			mod_location = (File) data.get("coremodLocation");
		}
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	public static class Setup implements IFMLCallHook
	{
		@Override
		public Void call() throws Exception {
			
			if(mod_location != null)
			{
				String path = mod_location.getParentFile().getAbsolutePath();
				
				File black = new File(path+"/ecomodcompat_transform_blacklist.txt");
				
				if(!black.exists())
				{
					if(black.createNewFile())
					{
						FileUtils.writeStringToFile(black, "#EcologyModCompat ASM transformation blacklist\n#Write Class names in the following form(Every entry must be in a new line):\n#class:[class name]\n#For example:\n#class:blusunrize.immersiveengineering.common.blocks.metal.TileEntityDieselGenerator", "UTF-8");
					}
				}
				else
				{
					String data = FileUtils.readFileToString(black, "UTF-8");
					
					for(String s : data.split("\n"))
					{
						if(s.startsWith("#"))
							continue;
						
						if(s.startsWith("class:"))
						{
							EMCClassTransformer.blacklisted_classes.add(s.substring(6));
						}
					}
				}
			}
			
			return null;
		}

		@Override
		public void injectData(Map<String, Object> data) {
			
		}
	}
	
}
