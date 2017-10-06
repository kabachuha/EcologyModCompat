package ecomodcompat.core;

import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

public class EMCConsts 
{
	public static final String MODID = "ecomodcompat";
	public static final String NAME = "EcologyMod|Compat";
	public static final String VESRION = "1.0.0.0";
	public static final String ECOMOD_ID = "ecomod";
	
	public static final ArtifactVersion ECOMOD_AS_ARTIFACT = new DefaultArtifactVersion(EMCConsts.ECOMOD_ID, true);
	
	public static final String URL = "";
	public static final String ECOMOD_URL = "https://minecraft.curseforge.com/projects/ecology-mod";
	
	public static boolean transformer_inited = false;
}
