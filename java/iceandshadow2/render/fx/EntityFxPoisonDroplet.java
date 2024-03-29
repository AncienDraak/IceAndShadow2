package iceandshadow2.render.fx;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/*
 * This was mostly liberally copied and pasted from the EntityDropParticle effect.
 */
public class EntityFxPoisonDroplet extends EntityFX {

	/** the material type for dropped items/blocks */
	private final Material materialType;

	/** The height of the current bob */
	private int bobTimer;

	public EntityFxPoisonDroplet(World par1World, double par2, double par4,
			double par6) {
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);

		this.particleRed = 0.0F;
		this.particleGreen = 0.5F;
		this.particleBlue = 0.1F;

		setParticleTextureIndex(113);
		setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.materialType = Material.water;
		this.bobTimer = 40;
		this.particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
		this.motionX = this.motionY = this.motionZ = 0.0D;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.motionY -= this.particleGravity;

		if (this.bobTimer-- > 0) {
			this.motionX *= 0.02D;
			this.motionY *= 0.02D;
			this.motionZ *= 0.02D;
			setParticleTextureIndex(113);
		} else {
			setParticleTextureIndex(112);
		}

		moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.particleMaxAge-- <= 0) {
			setDead();
		}

		if (this.isCollidedVertically && this.onGround) {
			setDead();
			// this.worldObj.spawnParticle("splash", this.posX, this.posY,
			// this.posZ, 0.0D, 0.0D, 0.0D);
		}

		final Material material = this.worldObj.getBlock(
				MathHelper.floor_double(this.posX),
				MathHelper.floor_double(this.posY),
				MathHelper.floor_double(this.posZ)).getMaterial();

		if (material.isLiquid() || material.isSolid()) {
			final double d0 = MathHelper.floor_double(this.posY)
					+ 1
					- BlockLiquid.getLiquidHeightPercent(this.worldObj
							.getBlockMetadata(
									MathHelper.floor_double(this.posX),
									MathHelper.floor_double(this.posY),
									MathHelper.floor_double(this.posZ)));

			if (this.posY < d0) {
				setDead();
			}
		}
	}
}
