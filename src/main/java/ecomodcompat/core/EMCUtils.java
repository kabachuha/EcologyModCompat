package ecomodcompat.core;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class EMCUtils
{
	public static <V> Set<V> putToSet(Set<V> set, V...values)
	{
		for(V v : values)
			set.add(v);
		
		return set;
	}
	
	public static String sumStringArray(String[] strarr, String splitter)
	{
		String ret = "";
		for(int i = 0; i < strarr.length; i++)
		{
			ret += strarr[i];
			if(i != strarr.length - 1)
				ret+=splitter;
		}
		
		return ret;
	}
	
	public static Pair<Integer, Integer> blockPosToPair(BlockPos pos)
	{
		return Pair.of(pos.getX() >> 4, pos.getZ() >> 4);
	}
	
	public static Pair<Integer, Integer> chunkPosToPair(ChunkPos pos)
	{
		return Pair.of(pos.x, pos.z);
	}
}
