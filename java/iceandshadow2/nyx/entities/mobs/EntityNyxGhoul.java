package iceandshadow2.nyx.entities.mobs;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iceandshadow2.IaSFlags;
import iceandshadow2.api.IIaSTool;
import iceandshadow2.api.IaSToolMaterial;
import iceandshadow2.nyx.NyxItems;
import iceandshadow2.util.IaSEntityHelper;
import iceandshadow2.util.IaSWorldHelper;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityNyxGhoul extends EntityZombie implements IIaSMobGetters {

	private EntityLivingBase searched;

	protected static double moveSpeed = 0.25;

	protected int regenDelay;

	public EntityNyxGhoul(World par1World) {
		super(par1World);

		this.experienceValue = 25;
		this.regenDelay = 0;

		this.tasks.taskEntries.clear();
		this.targetTasks.taskEntries.clear();

		this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityAnimal.class, 1.0D, false));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityNyxSkeleton.class, 1.0D, true));
		this.tasks.addTask(5, new EntityAIFleeSun(this,
				EntityNyxGhoul.moveSpeed + 0.5));
        //this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWander(this,
				EntityNyxGhoul.moveSpeed));
		this.tasks.addTask(8, new EntityAIWatchClosest(this,
				EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityNyxSkeleton.class, 0, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityAnimal.class, 0, true));
	}

	@Override
	protected void addRandomArmor() {
		return;
	}

	@Override
	public float getBrightness(float par1) {
		return super.getBrightness(par1) * 0.5F + 0.5F;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float par1) {
		return super.getBrightnessForRender(par1) / 2 + Integer.MAX_VALUE / 2;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth)
		.setBaseValue(getScaledMaxHealth());
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance)
		.setBaseValue(0.66D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed)
		.setBaseValue(EntityNyxGhoul.moveSpeed);
		getEntityAttribute(SharedMonsterAttributes.followRange)
		.setBaseValue(24.0);
		getEntityAttribute(EntityZombie.field_110186_bp)
		.setBaseValue(0.0);
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {
		final float f = getAttackStrength(par1Entity);
		final int i = 0;

		final boolean flag = par1Entity.attackEntityFrom(
				DamageSource.causeMobDamage(this), f * 2 + 4);

		if (flag) {

			final int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0)
				par1Entity.setFire(j * 4);

			if (par1Entity instanceof EntityLivingBase && !this.worldObj.isRemote) {
				final EntityLivingBase armored = (EntityLivingBase)par1Entity;
				for(int s = 4; s >= 1; --s) {
					if(armored.getEquipmentInSlot(s) != null) {
						IaSEntityHelper.dropItem(this, armored.getEquipmentInSlot(s));
						armored.setCurrentItemOrArmor(s, null); //What the hell.
						break;
					}
				}
				armored.addPotionEffect(new PotionEffect(Potion.wither.id,
					75+IaSWorldHelper.getDifficulty(this.worldObj)*30,0));
			}
		}

		return flag;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (isEntityInvulnerable()
				|| !par1DamageSource.isUnblockable()
				|| par1DamageSource == DamageSource.drown)
			return false;
		boolean flag;
		if(par1DamageSource.isMagicDamage() && !par1DamageSource.isDamageAbsolute())
			par2 -= IaSWorldHelper.getRegionArmorMod(this);
		if(par1DamageSource.isDamageAbsolute() && !isInvisible())
			flag = super.attackEntityFrom(par1DamageSource, par2);
		else
			flag = super.attackEntityFrom(par1DamageSource, par2/2);
		if(par1DamageSource instanceof EntityDamageSource && !par1DamageSource.isProjectile()
				&& !par1DamageSource.isMagicDamage()
				&& !par1DamageSource.isExplosion()
				&& !par1DamageSource.isFireDamage()) {
			final Entity ent = ((EntityDamageSource)par1DamageSource).getEntity();
			if(ent instanceof EntityPlayer)
				((EntityPlayer)ent).dropOneItem(false);
		}
		return flag;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2) {
		if (!par1 || this.worldObj.isRemote)
			return;

		if(this.rand.nextInt(4-(IaSWorldHelper.getDifficulty(this.worldObj)>=3?1:0)) == 0)
			dropItem(NyxItems.boneSanctified, 1);
		else if(IaSWorldHelper.getDifficulty(this.worldObj)<3)
			dropItem(NyxItems.alabaster, 1);
		if(IaSWorldHelper.getDifficulty(this.worldObj)>=3)
			dropItem(NyxItems.alabaster, 1);
	}

	@Override
	protected void dropRareDrop(int par1) {
	}

	@Override
	protected void fall(float par1) {
		super.fall(par1 / 3.0F);
	}

	public int getAttackStrength(Entity par1Entity) {
		final ItemStack var2 = getHeldItem();
		int var3;
		if (this.worldObj != null)
			var3 = IaSWorldHelper.getDifficulty(this.worldObj) >= 3 ? 8 : 9;
		else
			var3 = 8;

		if (var2 != null) {
			if (var2.getItem() instanceof IIaSTool)
				var3 += MathHelper.ceiling_float_int(IaSToolMaterial
						.extractMaterial(var2).getToolDamage(var2, this,
								par1Entity));
		}
		return var3;
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.posY > 48.0F && super.getCanSpawnHere();
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		if(getAttackTarget() != null)
			return "IceAndShadow2:mob_nyxghoul_idle";
		return null;
	}

	@Override
	protected String getHurtSound() {
		return "IceAndShadow2:mob_nyxghoul_hurt";
	}

	@Override
	protected String getDeathSound() {
		return "IceAndShadow2:mob_nyxghoul_death";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		if (!isInvisible())
			playSound("step.snow", 1.0F, 1.0F);
	}

	@Override
	public void onKillEntity(EntityLivingBase misnomer) {
		heal(misnomer.getMaxHealth()/2);
		super.onKillEntity(misnomer);
	}

	@Override
	public double getMoveSpeed() {
		return EntityNyxGhoul.moveSpeed;
	}

	@Override
	public double getScaledMaxHealth() {
		return 50.0D;
	}

	@Override
	public EntityLivingBase getSearchTarget() {
		return this.searched;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.worldObj.isRemote)
			return;
		final boolean attacking = getAttackTarget() != null;
		if (--this.regenDelay <= 0) {
			if(IaSWorldHelper.getDifficulty(this.worldObj) <= 1) {
				setDead();
				return;
			}
			heal(1);
			this.regenDelay = 15;
			if(attacking) {
				final List li = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBox.expand(18, 18, 18));
				li.addAll(this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, this.boundingBox.expand(18, 18, 18)));
				li.add(getAttackTarget());
				for(final Object ent : li)
					((EntityLivingBase)ent).addPotionEffect(new PotionEffect(Potion.blindness.id,50,0));
			}
		}
		if(attacking) {
			final double dist = getDistanceSqToEntity(getAttackTarget());
			if(isInvisible() && dist < 9.0)
				setInvisible(false);
			else if(!isInvisible() && dist > 16.0)
				setInvisible(true);
		} else
			setInvisible(true);
	}

	@Override
	public int getTotalArmorValue() {
		return super.getTotalArmorValue()+IaSWorldHelper.getRegionArmorMod(this);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData dat) {
		this.equipmentDropChances[0] = 0.0F;

		IaSWorldHelper.getDifficulty(this.worldObj);
		return dat;
	}

	@Override
	public void setFire(int time) {
		if (this.dimension != IaSFlags.dim_nyx_id)
			super.setFire(time);
	}

	@Override
	public void setInWeb() {
		//Nope.
	}

	@Override
	public void setSearchTarget(EntityLivingBase ent) {
		this.searched = ent;
	}

}
