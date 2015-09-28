package iceandshadow2.nyx.world;

import iceandshadow2.util.IaSWorldHelper;
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
	public int[] getInts(int x, int z, int xlim, int zlim) {
		final int[] var6 = IntCache.getIntCache(xlim * zlim);
		int rl = IaSWorldHelper.getRegionLevel(null, x*16, -1, z*16);
		for (int zit = 0; zit < zlim; ++zit) {
			for (int xit = 0; xit < xlim; ++xit) {
				this.initChunkSeed(xit + x, zit + z);
				int nb = this.nextInt(this.allowedBiomes.length);
				if (this.allowedBiomes[nb] == NyxBiomes.nyxInfested) {
					if(rl < 1)
						nb = NyxBiomes.nyxHillForest.biomeID;
					else if(rl < 3)
						nb = NyxBiomes.nyxMesaForest.biomeID;
					else 
						nb = this.allowedBiomes[nb].biomeID;
				} else if (this.allowedBiomes[nb] == NyxBiomes.nyxRugged) {
					if(rl < 1)
						nb = NyxBiomes.nyxHills.biomeID;
					else if(rl < 2)
						nb = NyxBiomes.nyxHighMountains.biomeID;
					else if(rl < 5)
						nb = NyxBiomes.nyxMesas.biomeID;
					else
						nb = this.allowedBiomes[nb].biomeID;
				} else
					nb = this.allowedBiomes[nb].biomeID;
				var6[xit + zit * xlim] = nb;
			}
		}

		return var6;
	}
}
