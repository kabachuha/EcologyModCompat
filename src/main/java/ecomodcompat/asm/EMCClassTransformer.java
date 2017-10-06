package ecomodcompat.asm;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import ecomodcompat.core.EMCConsts;
import ecomodcompat.core.EMCUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.classloading.FMLForgePlugin;
import net.minecraftforge.fluids.Fluid;

public class EMCClassTransformer implements IClassTransformer {
	
	static Logger log = LogManager.getLogger("EcomodCompatASM");
	
	private static final boolean DEBUG = false;
	
	public static List<String> failed_transformers = new ArrayList<String>();
	public static List<String> failed_tiles = new ArrayList<String>();

	public EMCClassTransformer()
	{
		EMCConsts.transformer_inited = true;
		log.info("Initializing EMCClassTransformer");
	}
	
	public static final String IE_TE_METAL = "blusunrize.immersiveengineering.common.blocks.metal";
	public static final String IE_TE_STONE = "blusunrize.immersiveengineering.common.blocks.stone";
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(transformedName.equals(IE_TE_METAL+".TileEntityMultiblockMetal$MultiblockProcess"))
			return handleIETEMultiblockMetal(transformedName, basicClass);
		
		if(transformedName.equals(IE_TE_METAL+".TileEntityAssembler"))
			return handleIEAssembler(transformedName, basicClass);
		
		if(transformedName.equals(IE_TE_METAL+".TileEntityExcavator"))
			return handleIEExcavator(transformedName, basicClass);
		
		if(transformedName.equals(IE_TE_METAL+".TileEntityDieselGenerator"))
			return handleIEDieselGenerator(transformedName, basicClass);
		
		if(transformedName.equals(IE_TE_STONE+".TileEntityAlloySmelter"))
			return handleIEBlastFurnaceAlloySmelter(transformedName, basicClass);
		
		if(transformedName.equals(IE_TE_STONE+".TileEntityBlastFurnace"))
			return handleIEBlastFurnaceAlloySmelter(transformedName, basicClass);
		
		return basicClass;
	}
	
	private byte[] handleIETEMultiblockMetal(String name, byte[] bytecode)
	{
		log.info("Transforming "+name);
		log.info("Initial size: "+bytecode.length+" bytes");

		byte[] bytes = bytecode.clone();
		
		try
		{
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			
			if(DEBUG)
				printClassInfo(name, classNode);
			
			MethodNode mn = getMethod(classNode, "doProcessTick", "doProcessTick", "(L"+toInternal(IE_TE_METAL+".TileEntityMultiblockMetal")+";)V", "(L"+toInternal(IE_TE_METAL+".TileEntityMultiblockMetal")+";)V");
			
			if(DEBUG)
				printMethodInfo(mn);
			
			InsnList lst = new InsnList();
		
			lst.add(new VarInsnNode(Opcodes.ALOAD, 1));
			lst.add(new VarInsnNode(Opcodes.ILOAD, 3));
			lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/EMCASMHooks", "handle_IE_TEMulti_Process_doProcessTick", "(Ljava/lang/Object;I)V", false));
			lst.add(new LabelNode());
			
			mn.instructions.insert(mn.instructions.getLast().getPrevious().getPrevious(), lst);
			
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
			
			failed_tiles.add("immersiveengineering:crusher");
			failed_tiles.add("immersiveengineering:fermenter");
			failed_tiles.add("immersiveengineering:refinery");
			failed_tiles.add("immersiveengineering:arcfurnace");
			
			return bytecode;
		}
	}
	
	private byte[] handleIEExcavator(String name, byte[] bytecode)
	{
		log.info("Transforming "+name);
		log.info("Initial size: "+bytecode.length+" bytes");

		byte[] bytes = bytecode.clone();
		
		try
		{
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			
			if(DEBUG)
				printClassInfo(name, classNode);
			
			MethodNode mn = getMethod(classNode, "digBlock", "digBlock", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/item/ItemStack;", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/item/ItemStack;");
			
			if(DEBUG)
				printMethodInfo(mn);
			
			InsnList lst = new InsnList();
		
			lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
			lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/EMCASMHooks", "handle_IE_Excavator_digBlock", "(Ljava/lang/Object;)V", false));
			lst.add(new LabelNode());
			
			mn.instructions.insert(mn.instructions.getLast().getPrevious().getPrevious(), lst);
			
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
			failed_tiles.add("immersiveengineering:excavator");
			
			return bytecode;
		}
	}
	
	private byte[] handleIEBlastFurnaceAlloySmelter(String name, byte[] bytecode)
	{
		log.info("Transforming "+name);
		log.info("Initial size: "+bytecode.length+" bytes");

		byte[] bytes = bytecode.clone();
		
		try
		{
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			
			if(DEBUG)
				printClassInfo(name, classNode);
			
			MethodNode mn = getMethod(classNode, "func_73660_a", "func_73660_a", "()V", "()V");
			
			if(DEBUG)
				printMethodInfo(mn);
			
			InsnList lst = new InsnList();
		
			
			lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
			lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
			lst.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "active", "Z"));
			lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/EMCASMHooks", "handle_IE_TileEntity_Tick_WithCondition", "(Ljava/lang/Object;Z)V", false));
			lst.add(new LabelNode());
			
			AbstractInsnNode[] ain = mn.instructions.toArray();
			int insertion_index = -1;
			
			for(int i = 0; i < ain.length; i++)
			{
				AbstractInsnNode insn = ain[i];
				
				if(insn instanceof FieldInsnNode)
				{
					FieldInsnNode min = (FieldInsnNode)insn;
					if(min.getOpcode() == Opcodes.GETFIELD && min.owner.equals(classNode.name) && min.name.equals("active") && min.desc.equals("Z"))
					{
						insertion_index = i + 2;
						break;
					}
				}
			}
			
			if(insertion_index != -1)
			{
				mn.instructions.insert(mn.instructions.get(insertion_index), lst);
			}
			else
			{
				failed_transformers.add(name);
				
				if(name.equals(IE_TE_STONE+".TileEntityAlloySmelter"))
				{
					failed_tiles.add("immersiveengineering:alloysmelter");
				}
				else
				{
					failed_tiles.add("immersiveengineering:blastfurnace");
					failed_tiles.add("immersiveengineering:blastfurnaceadvanced");
				}
				
				return bytecode;
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
			
			if(name.equals(IE_TE_STONE+".TileEntityAlloySmelter"))
			{
				failed_tiles.add("immersiveengineering:alloysmelter");
			}
			else
			{
				failed_tiles.add("immersiveengineering:blastfurnace");
				failed_tiles.add("immersiveengineering:blastfurnaceadvanced");
			}
			
			return bytecode;
		}
	}
	
	private byte[] handleIEDieselGenerator(String name, byte[] bytecode)
	{
		log.info("Transforming "+name);
		log.info("Initial size: "+bytecode.length+" bytes");

		byte[] bytes = bytecode.clone();
		
		try
		{
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			
			if(DEBUG)
				printClassInfo(name, classNode);
			
			MethodNode mn = getMethod(classNode, "func_73660_a", "func_73660_a", "()V", "()V");
			
			if(DEBUG)
				printMethodInfo(mn);
			
			InsnList lst = new InsnList();
		
			lst.add(new LabelNode());
			lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
			lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/EMCASMHooks", "handle_IE_TileEntity_Tick", "(Ljava/lang/Object;)V", false));
			
			AbstractInsnNode[] ain = mn.instructions.toArray();
			int insertion_index = -1;
			
			for(int i = 0; i < ain.length; i++)
			{
				AbstractInsnNode insn = ain[i];
				
				if(insn instanceof MethodInsnNode)
				{
					MethodInsnNode min = (MethodInsnNode)insn;
					printMethodInsnInfo(min);
					if(min.getOpcode() == Opcodes.INVOKEVIRTUAL && min.owner.equals("net/minecraftforge/fluids/FluidTank") && min.name.equals("drain") && min.desc.equals("(IZ)Lnet/minecraftforge/fluids/FluidStack;") && (min.itf == false))
					{
						log.info("FOUND:  INVOKEVIRTUAL net/minecraftforge/fluids/FluidTank.drain (IZ)Lnet/minecraftforge/fluids/FluidStack;!!!!!");
						insertion_index = i - 7;
						break;
					}
				}
				
				if(insn instanceof VarInsnNode)
				{
					printVarInsnInfo((VarInsnNode)insn);
				}
				
				if(insn instanceof LabelNode)
				{
					log.info("LABEL");
				}
			}
			
			if(insertion_index != -1)
			{
				mn.instructions.insert(mn.instructions.get(insertion_index), lst);
			}
			else
			{
				failed_transformers.add(name);
				failed_tiles.add("immersiveengineering:dieselgenerator");
				log.error("NOT FOUND:  INVOKEVIRTUAL net/minecraftforge/fluids/FluidTank.drain (IZ)Lnet/minecraftforge/fluids/FluidStack;");
				return bytecode;
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
			failed_tiles.add("immersiveengineering:dieselgenerator");
			
			return bytecode;
		}
	}
	
	private byte[] handleIEAssembler(String name, byte[] bytecode)
	{
		log.info("Transforming "+name);
		log.info("Initial size: "+bytecode.length+" bytes");

		byte[] bytes = bytecode.clone();
		
		try
		{
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			
			if(DEBUG)
				printClassInfo(name, classNode);
			
			MethodNode mn = getMethod(classNode, "doProcessOutput", "doProcessOutput", "(Lnet/minecraft/item/ItemStack;)V", "(Lnet/minecraft/item/ItemStack;)V");
			
			if(DEBUG)
				printMethodInfo(mn);
			
			InsnList lst = new InsnList();
		
			lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
			lst.add(new VarInsnNode(Opcodes.ALOAD, 1));
			lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ecomodcompat/asm/EMCASMHooks", "handle_IE_Assembler_doProcessOutput", "(Ljava/lang/Object;Lnet/minecraft/item/ItemStack;)V", false));
			lst.add(new LabelNode());
			
			mn.instructions.insert(mn.instructions.getLast().getPrevious().getPrevious(), lst);
			
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
			failed_tiles.add("immersiveengineering:assembler");
			
			return bytecode;
		}
	}

	public static String chooseByEnvironment(String deobf, String obf)
	{
		return FMLForgePlugin.RUNTIME_DEOBF ? obf : deobf;
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
	
	public static final String REGEX_NOTCH_FROM_MCP = "!&!";
	
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
	
	public static String toInternal(String name)
	{
		return name.replace('.', '/');
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
		}
		log.info("Inner classes: ");
		clazz.innerClasses.forEach((n) -> { log.info(n.name);});
		log.info("----------------------------------------------------------------------------");	
	}
	
	public static void printMethodInfo(MethodNode method)
	{
		log.info("****************************************************************************");
		log.info("Method : ");
		log.info("Name: "+method.name);
		log.info("Description: "+method.desc);
		log.info("Signature: "+method.signature);
		log.info("Exceptions: "+EMCUtils.sumStringArray(method.exceptions.toArray(new String[method.exceptions.size()]), ", "));
		if(method.visibleAnnotations != null && method.visibleAnnotations.size() > 0)
		{
			log.info("With annotations:");
			for(AnnotationNode an : method.visibleAnnotations)
			{
				log.info(an.desc);
				log.info(an.values);
			}
		}
		log.info("****************************************************************************");
	}
	
	public static void printMethodInsnInfo(MethodInsnNode method)
	{
		log.info("****************************************************************************");
		log.info("MethodInsnNode:");
		log.info("Owner: "+method.owner);
		log.info("Name: "+method.name);
		log.info("Description: "+method.desc);
		log.info("****************************************************************************");
	}
	
	public static void printVarInsnInfo(VarInsnNode var)
	{
		log.info("VarInsnNode: " + var.getOpcode() + " : " + var.var);
	}
}
