package ecomodcompat.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class EMCLoadingPlugin implements IFMLLoadingPlugin {

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
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{

	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

}
