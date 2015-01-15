package iceandshadow2.nyx.world;

import iceandshadow2.nyx.world.biome.NyxBiome;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerNyxRandomBiomes extends GenLayer {
	/** This sets all the biomes that are allowed to appear in a dimension */
	private final BiomeGenBase[] allowedBiomes;

	public GenLayerNyxRandomBiomes(BiomeGenBase[] biomes, long par1) {
		super(par1);
		this.allowedBiomes = biomes;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be
	 * interpreted as temperatures, rainfall amounts, or biomeList[] indices
	 * based on the particular GenLayer subclass.
	 */
	@Override
	public int[] getInts(int par1, int par2, int par3, int par4) {
		final int[] var6 = IntCache.getIntCache(par3 * par4);

		for (int var7 = 0; var7 < par4; ++var7) {
			for (int var8 = 0; var8 < par3; ++var8) {
				this.initChunkSeed(var8 + par1, var7 + par2);
				int nb = this.nextInt(this.allowedBiomes.length);
				if (this.allowedBiomes[nb] instanceof NyxBiome
						&& ((NyxBiome) this.allowedBiomes[nb]).isRare())
					nb = this.nextInt(this.allowedBiomes.length);
				var6[var8 + var7 * par3] = this.allowedBiomes[nb].biomeID;
			}
		}

		return var6;
	}
}
