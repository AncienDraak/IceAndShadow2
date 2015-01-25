package iceandshadow2.nyx.items;

import iceandshadow2.EnumIaSModule;
import iceandshadow2.ias.interfaces.IIaSGlowing;
import iceandshadow2.ias.items.IaSBaseItemSingle;
import iceandshadow2.nyx.entities.projectile.EntityShadowBall;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NyxItemBoneCursed extends IaSBaseItemSingle implements IIaSGlowing {

	@SideOnly(Side.CLIENT)
	private IIcon glow;

	public NyxItemBoneCursed(String texName) {
		super(EnumIaSModule.NYX, texName);
		this.setMaxStackSize(16);
		this.setFull3D();
	}

	@Override
	public int getFirstGlowPass(ItemStack is) {
		return 1;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if (pass == 1)
			return glow;
		return this.itemIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int metadata) {
		return 2;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1Stack, World par2World,
			EntityPlayer player) {
		if (!par2World.isRemote)
			par2World.spawnEntityInWorld(new EntityShadowBall(par2World,
					player, true, true));
		if (!player.capabilities.isCreativeMode) {
			par1Stack.stackSize -= 1;
			player.attackEntityFrom(DamageSource.magic,
					player.worldObj.difficultySetting.getDifficultyId());
		}
		return par1Stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		this.itemIcon = reg.registerIcon(this.getTexName());
		glow = reg.registerIcon(this.getTexName() + "Glow");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public boolean usesDefaultGlowRenderer() {
		return true;
	}
}
