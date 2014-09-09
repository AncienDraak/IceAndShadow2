package iceandshadow2.nyx.world.biome;

import iceandshadow2.nyx.NyxBlocks;

import java.util.Random; //Fuck you, Scala.

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class NyxBiome extends BiomeGenBase {

	private boolean rare;

	public boolean isRare() {
		return rare;
	}

	public NyxBiome(int par1, boolean register, float heightRoot, float heightVari, boolean isRare) {
		super(par1, register);
		this.setHeight(new Height(heightRoot, heightVari));
		this.setTemperatureRainfall(0.0F, 0.0F);
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.biomeName = "Nyx";
		this.topBlock = Blocks.snow_layer;
		this.fillerBlock = Blocks.snow;
		rare = isRare;
	}

	protected void genStructures(World par1World, Random par2Random, int xchunk, int zchunk) {

	}

	@Override
	public void decorate(World par1World, Random par2Random, int xchunk, int zchunk) {
		//genStructures(par1World, par2Random, xchunk, zchunk);
		return;
	}

	public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] meta, int a, int b, double c)
	{
		boolean flag = true;
		Block block = this.topBlock;
		byte b0 = (byte)(this.field_150604_aj & 255);
		Block block1 = this.fillerBlock;
		int k = -1;
		int l = (int)(c / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int i1 = a & 15;
		int j1 = b & 15;
		int k1 = blocks.length / 256;

		for (int l1 = 255; l1 >= 0; --l1)
		{
			int i2 = (j1 * 16 + i1) * k1 + l1;

			if (l1 <= 0 + rand.nextInt(3))
			{
				blocks[i2] = Blocks.bedrock;
			}
			else
			{
				Block block2 = blocks[i2];

				if (block2 != null && block2.getMaterial() != Material.air)
				{
					if (block2 == NyxBlocks.stone)
					{
						if (k == -1)
						{
							if (l <= 0)
							{
								block = null;
								b0 = 0;
								block1 = NyxBlocks.stone;
							}
							else if (l1 >= 59 && l1 <= 64)
							{
								block = this.topBlock;
								b0 = (byte)(this.field_150604_aj & 255);
								block1 = this.fillerBlock;
							}

							if (l1 < 63 && (block == null || block.getMaterial() == Material.air))
							{
								if (this.getFloatTemperature(a, l1, b) < 0.15F)
								{
									block = Blocks.ice;
									b0 = 0;
								}
								else
								{
									block = Blocks.water;
									b0 = 0;
								}
							}

							k = l;

							if (l1 >= 62)
							{
								blocks[i2] = block;
								meta[i2] = b0;
							}
							else if (l1 < 56 - l)
							{
								block = null;
								block1 = NyxBlocks.stone;
								blocks[i2] = NyxBlocks.unstableIce;
							}
							else
							{
								blocks[i2] = block1;
							}
						}
						else if (k > 0)
						{
							--k;
							blocks[i2] = block1;
						}
					}
				}
				else
				{
					k = -1;
				}
			}
		}
	}

}
