package iceandshadow2.nyx.blocks;

import java.util.Random;

import iceandshadow2.EnumIaSModule;
import iceandshadow2.ias.blocks.IaSBaseBlockSingle;
import iceandshadow2.render.fx.IaSFxManager;
import iceandshadow2.util.IaSBlockHelper;
import iceandshadow2.util.IaSPlayerHelper;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NyxBlockCryingObsidian extends IaSBaseBlockSingle {
	public NyxBlockCryingObsidian(String id) {
		super(EnumIaSModule.NYX, id, Material.rock);
		setLuminescence(1.0F);
		setLightColor(0.9F, 0.8F, 1.0F);
		setResistance(2000.0F);
		this.setHarvestLevel("pickaxe", 3);
	}

	@Override
	public float getBlockHardness(World w, int x,
			int y, int z) {
		return (w.getBlockMetadata(x, y, z)!=0?-1:Blocks.obsidian.getBlockHardness(w, x, y, z));
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world,
			int x, int y, int z) {
		return type != EnumCreatureType.monster;
	}

	@Override
	public int damageDropped(int par1) {
		return 0;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2,
			int par3, int par4) {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World,
			int par2, int par3, int par4) {
		final float var5 = 0.0125F;
		return AxisAlignedBB.getBoundingBox(par2 + var5, par3, par4 + var5,
				par2 + 1 - var5, par3 + 1 - var5, par4 + 1 - var5);
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int x, int y, int z,
			Entity par5Entity) {
		if (par5Entity instanceof EntityLivingBase
				&& !(par5Entity instanceof EntityMob)) {
			final EntityLivingBase elb = (EntityLivingBase) par5Entity;
			if (!elb.isPotionActive(Potion.resistance)) {
				elb.addPotionEffect(new PotionEffect(23, 1, 0));
				elb.addPotionEffect(new PotionEffect(Potion.resistance.id, 39,
						3));
			}
			if(!elb.isPotionActive(Potion.regeneration))
				elb.addPotionEffect(new PotionEffect(Potion.regeneration.id,51,1));
			if (elb.isSneaking()) {
				if (elb.isPotionActive(Potion.confusion))
					elb.addPotionEffect(new PotionEffect(Potion.confusion.id,
							2, 0));
				else if (elb instanceof EntityPlayer) {
					final EntityPlayer playuh = (EntityPlayer) elb;
					for (int xit = -1; xit <= 1; ++xit) {
						for (int zit = -1; zit <= 1; ++zit) {
							if (par1World.getBlock(x + xit, y, z + zit) != this)
								return;
						}
					}
					if (!IaSBlockHelper.isTransient(par1World, x, y + 1, z))
						return;
					if (!IaSBlockHelper.isTransient(par1World, x, y + 2, z))
						return;
					IaSPlayerHelper
					.messagePlayer(playuh,
							"You feel something bind your life force to the obsidian....");
					playuh.setSpawnChunk(new ChunkCoordinates(x, y + 1, z),
							true);
					if (playuh.getHealth() > 2.0F)
						playuh.attackEntityFrom(DamageSource.magic, 1);
					elb.addPotionEffect(new PotionEffect(
							Potion.confusion.id, 2, 0));
				}
			}
		}
	}


	@Override
	public void randomDisplayTick(World par1World, int par2, int par3,
			int par4, Random par5Random) {
		if (par1World.getBlockMetadata(par2, par3, par4) == 0)
			return;

		final double var9 = par3 + par5Random.nextFloat();
		double var13 = 0.0D;
		double var15 = 0.0D;
		double var17 = 0.0D;
		final int var19 = par5Random.nextInt(2) * 2 - 1;
		final int var20 = par5Random.nextInt(2) * 2 - 1;
		var13 = (par5Random.nextFloat() - 0.5D) * 0.125D;
		var15 = (par5Random.nextFloat() - 0.5D) * 0.125D;
		var17 = (par5Random.nextFloat() - 0.5D) * 0.125D;
		final double var11 = par4 + 0.5D + 0.25D * var20;
		var17 = par5Random.nextFloat() * 1.0F * var20;
		final double var7 = par2 + 0.5D + 0.25D * var19;
		var13 = par5Random.nextFloat() * 1.0F * var19;
		IaSFxManager.spawnParticle(par1World, "vanilla_portal", var7, var9, var11,
				var13, var15, var17, false, true);
	}

}
