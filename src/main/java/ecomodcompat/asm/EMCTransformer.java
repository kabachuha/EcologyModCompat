package ecomodcompat.asm;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import ecomodcompat.core.CConsts;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.classloading.FMLForgePlugin;

public class EMCTransformer implements IClassTransformer
{
	private static final boolean DEBUG = true;
	static Logger log = LogManager.getLogger("EcomodCompatASM");
	
	public static List<String> failed_transformers = new ArrayList<String>();
	
	public static boolean setupCompleted = false;
	
	public EMCTransformer()
	{
		CConsts.asm_transformer_inited = true;
		log.info("Initializing EcomodCompat ASM transformer.");
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(transformedName.contains("ecomodcompat.asm"))//Do not transform yourself!
			return basicClass;
		
		if(!setupCompleted)
			return basicClass;
		
		basicClass = tryWeather2Transform(transformedName, basicClass);
		
		return basicClass;
	}
	
	private byte[] tryWeather2Transform(String name, byte[] bytecode)
	{
		if(W2Helper.w2transformers.contains(name))
		{
			if(CConsts.HardCompatInfo.weather2_acid_rain_patch)
			{
				byte[] bytes = bytecode.clone();
				
				log.info("Transforming "+name);
				log.info("Initial size: "+bytecode.length+" bytes");
				
				try
				{
					ClassNode classNode = new ClassNode();
					ClassReader classReader = new ClassReader(bytes);
					classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
					ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				
					if(DEBUG)
						printClassInfo(name, classNode);
				
					if(name.equals(W2Helper.C_storm_object))
					{
						classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, W2Helper.F_acid, "Z", null, null));
						
						MethodNode mn = getMethod(classNode, "initFirstTime", "initFirstTime", "()V", "()V");
						
						InsnList lst = new InsnList();
						
						lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
						lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/Weather2Hooks", "so_initFirstTime", "(Lweather2/weathersystem/storm/StormObject;)V", false));
						lst.add(new LabelNode());
						
						mn.instructions.insert(mn.instructions.getLast().getPrevious().getPrevious(), lst);
						
						mn = getMethod(classNode, "nbtSyncFromServer", "nbtSyncFromServer", "()V", "()V");
						lst = new InsnList();
						
						lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
						lst.add(new VarInsnNode(Opcodes.ALOAD, 1));
						lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/Weather2Hooks", "so_nbtSyncFromServer", "(Lweather2/weathersystem/storm/StormObject;Lweather2/util/CachedNBTTagCompound;)V", false));
						lst.add(new LabelNode());
						
						mn.instructions.insert(mn.instructions.getLast().getPrevious().getPrevious(), lst);
						
						mn = getMethod(classNode, "nbtSyncForClient", "nbtSyncForClient", "()V", "()V");
						lst = new InsnList();
						
						lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
						lst.add(new VarInsnNode(Opcodes.ALOAD, 1));
						lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/Weather2Hooks", "so_nbtSyncForClient", "(Lweather2/weathersystem/storm/StormObject;Lweather2/util/CachedNBTTagCompound;)V", false));
						lst.add(new LabelNode());
						
						mn.instructions.insert(mn.instructions.getLast().getPrevious().getPrevious(), lst);
					}
					
					if(name.equals(W2Helper.C_render_forecast))
					{
						MethodNode mn = getMethod(classNode, "func_192841_a", "func_192841_a", "(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", "(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V");
						
						for(int i = 0; i < mn.instructions.size(); i++)
						{
							AbstractInsnNode n = mn.instructions.get(i);
							if(n instanceof MethodInsnNode)
							{
								MethodInsnNode nk = (MethodInsnNode)n;
								
								if(nk.name.equals("renderIconNew"))
								{
									if(n.getPrevious() instanceof FieldInsnNode)
									{
										FieldInsnNode o = (FieldInsnNode)n.getPrevious();
									
										if(o.owner.equals("weather2/ClientProxy") && (o.name.equals("radarIconTornado") || o.name.equals("radarIconCyclone") || o.name.equals("radarIconHail") || o.name.equals("radarIconLightning") || o.name.equals("radarIconRain")))
										{
											mn.instructions.remove(o);
											InsnList lst = new InsnList();
						
											lst.add(new VarInsnNode(Opcodes.ALOAD, 32));
											lst.add(o);
											lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/Weather2Hooks", "fore_getIcon", "(Lweather2/weathersystem/storm/StormObject;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", false));
										
											mn.instructions.insertBefore(nk, lst);
										}
									}
								}
							}
						}
					}
					
				
					classNode.accept(cw);
					bytes = cw.toByteArray();
				
					log.info("Transformed "+name);
					log.info("Final size: "+bytes.length+" bytes");
				
					return bytes;
				}
				catch(Exception e)
				{
					log.error("Unable to patch "+name+"!");
					log.error(e.toString());
					e.printStackTrace();
				
					failed_transformers.add(name);
				
					return bytecode;
				}
			}
		}
		
		
		return bytecode;
	}
	
	public static class W2Helper
	{
		static final String C_storm_object = "weather2.weathersystem.storm.StormObject";
		static final String C_render_forecast = "weather2.client.block.TileEntityWeatherForecastRenderer";
		public static final String F_acid = "is_acid";
		
		static final List<String> w2transformers = new ArrayList<String>();
		
		static
		{
			w2transformers.add(C_storm_object);
			w2transformers.add(C_render_forecast);
		}
	}
	
	
	//Utils:
	public static final String REGEX_NOTCH_FROM_MCP = "!&!";
	
	public static String chooseByEnvironment(String deobf, String obf)
	{
		return FMLForgePlugin.RUNTIME_DEOBF ? obf : deobf;
	}
	
	public static MethodNode getMethod(ClassNode cn, String deobfName, String obfName, String deobfDesc, String obfDesc)
	{
		String methodName = null;
		String methodDesc = null;
		if(!checkSameAndNullStrings(deobfName,obfName))
			methodName = chooseByEnvironment(deobfName,obfName);
		if(!checkSameAndNullStrings(deobfDesc,obfDesc))
			methodDesc = chooseByEnvironment(deobfDesc,obfDesc);
		
		String additionalMN = "";
		if(methodName != null && methodName.contains(REGEX_NOTCH_FROM_MCP))
		{
			String[] sstr = methodName.split(REGEX_NOTCH_FROM_MCP);
			methodName = sstr[0];
			additionalMN = sstr[1];
		}
		
		String additionalMD = "";
		if(methodDesc != null && methodDesc.contains(REGEX_NOTCH_FROM_MCP))
		{
			String[] sstr = methodDesc.split(REGEX_NOTCH_FROM_MCP);
			methodDesc = sstr[0];
			additionalMD = sstr[1];
		}
		
		if(checkSameAndNullStrings(methodName, methodDesc))
			return null;
		for(MethodNode mn : cn.methods)
			if((methodName == null || methodName.equals(mn.name) || additionalMN.equals(mn.name)) && (methodDesc == null || methodDesc.equals(mn.desc) || additionalMD.equals(mn.desc)))
				return mn;
		
		return null;
	}
	
	public static boolean checkSameAndNullStrings(String par1, String par2)
	{
		if(par1 == par2)
		{
			if(par1 == null && par2 == null)
				return true;
			else
				if(par1 != null && par2 != null)
					if(par1.isEmpty() && par2.isEmpty())
						return true;
		}
		
		return false;
	}
	
	public static boolean strictCompareByEnvironment(String name, String deobf, String obf)
	{
		String comparedTo = chooseByEnvironment(deobf.replace('/', '.'),obf.replace('/', '.'));
		return comparedTo.equalsIgnoreCase(name.replace('/', '.'));
	}
	
	public static void printClassInfo(String transformedName, ClassNode clazz)
	{
		log.info("----------------------------------------------------------------------------");
		log.info("Transformed class name "+transformedName);
		log.info("Class name "+clazz.name);
		log.info("-----------------------");
		log.info("Fields:");
		for(FieldNode field : clazz.fields)
		{
			log.info(field.name + "   of type   " + field.desc + " of access "+field.access);
		}
		log.info("-----------------------");
		log.info("Methods:");
		for(MethodNode mn : clazz.methods)
		{
			log.info(mn.name + " with desc "+mn.desc);
			if(mn.visibleAnnotations != null && mn.visibleAnnotations.size() > 0)
			{
				log.info("With annotations:");
				for(AnnotationNode an : mn.visibleAnnotations)
				{
					log.info(an.desc);
					log.info(an.values);
				}
			}
		}
		log.info("----------------------------------------------------------------------------");
		
	}
	
	public static String[] parseDeobfName(String name)
	{
		if(name != null && name.contains(REGEX_NOTCH_FROM_MCP))
		{
			String[] sstr = name.split(REGEX_NOTCH_FROM_MCP);
			if(sstr.length == 2)
			return sstr;
		}
		
		return new String[]{name, null};
	}
	
	public static boolean equalOneOfNames(String str, String deobf, String obf)
	{
		String name = null;
		if(!checkSameAndNullStrings(deobf,obf))
			name = chooseByEnvironment(deobf,obf);
		
		
		String def_obf = "";
		String notch_obf = "";
		if(name != null && name.contains(REGEX_NOTCH_FROM_MCP))
		{
			String[] sstr = name.split(REGEX_NOTCH_FROM_MCP);
			def_obf = sstr[0];
			notch_obf = sstr[1];
		}
		
		return str.equals(deobf) || str.equals(def_obf) || str.equals(notch_obf);
	}
}
