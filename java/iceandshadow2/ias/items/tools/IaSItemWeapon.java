package iceandshadow2.ias.items.tools;

import iceandshadow2.EnumIaSModule;
import iceandshadow2.IIaSModName;
import iceandshadow2.api.EnumIaSToolClass;
import iceandshadow2.api.IIaSTool;
import iceandshadow2.api.IaSRegistry;
import iceandshadow2.api.IaSToolMaterial;
import iceandshadow2.ias.interfaces.IIaSGlowing;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IaSItemWeapon extends ItemSword implements IIaSModName, IIaSTool, IIaSGlowing {

	private EnumIaSToolClass classe;
	protected IIcon invisible;
	
	public IaSItemWeapon(EnumIaSToolClass cl) {
		super(ToolMaterial.EMERALD);
		this.setUnlocalizedName("iasTool");
		classe = cl;
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		invisible = reg.registerIcon("IceAndShadow2:iasInvisible");
		//See IaSRegistry.
	}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(stack);
		return m.onSwing(stack, entityLiving);
	}

	@Override
	public void getSubItems(Item it, CreativeTabs ct,
			List l) {
		Collection<IaSToolMaterial> mats = IaSRegistry.getToolMaterials();
		l.add(new ItemStack(this));
		for(IaSToolMaterial m : mats) {
			ItemStack is;
			is = new ItemStack(this);
			is.setTagCompound(new NBTTagCompound());
			is.getTagCompound().setString("iasMaterial", m.getMaterialName());
			l.add(is.copy());
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack is, EntityPlayer user,
			Entity target) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(is);
		is.damageItem(m.onAttack(is, user, target), user);
		return true;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack is) {
		return IaSToolMaterial.extractMaterial(is).getRarity();
	}

	@Override
	public boolean onBlockDestroyed(ItemStack is, World w,
			Block bl, int x, int y,
			int z, EntityLivingBase user) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(is);
		if(m == null)
			return false;
		is.damageItem(m.onHarvest(is, user, w, x, y, z), user);
		return true;
	}

	@Override
	public int getItemEnchantability() {
		return 16;
	}

	@Override
	public String getToolMaterialName() {
		return "Wabbagoogies";
	}

	@Override
	public boolean getIsRepairable(ItemStack is, ItemStack two) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(is);
		if(m == null)
			return false;
		return m.isRepairable(is, two);
	}

	@Override
	public String getModName() {
		return this.getUnlocalizedName().substring(5);
	}

	@Override
	@Deprecated
	public String getTexName() {
		return null;
	}

	@Override
	public IIcon getIconIndex(ItemStack is) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(is);
		return m.getIcon(is);
	}

	@Override
	public String getUnlocalizedName(ItemStack is) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(is);
		if(m == null)
			return null;
		return m.getUnlocalizedName(is);
	}

	@Override
	public IIcon getIcon(ItemStack is, int renderPass, EntityPlayer player,
			ItemStack usingItem, int useRemaining) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(is);
		if(renderPass == 1 && !m.glows(this.getIaSToolClass()))
			return invisible;
		return m.getIcon(is);
	}
	
	@Override
	public IIcon getIcon(ItemStack is, int renderPass) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(is);
		if(renderPass == 1 && !m.glows(this.getIaSToolClass()))
			return invisible;
		return m.getIcon(is);
	}

	@Override
	public int getMaxDamage(ItemStack is) {
		IaSToolMaterial m = IaSToolMaterial.extractMaterial(is);
		if(m == null)
			return 0;
		return m.getDurability(is);
	}

	@Override
	public EnumIaSModule getIaSModule() {
		return EnumIaSModule.IAS;
	}
	
	@Override
	public EnumIaSToolClass getIaSToolClass() {
		return classe;
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int metadata) {
		return 2;
	}

	@Override
	public int getFirstGlowPass(ItemStack is) {
		return 1;
	}

	@Override
	public boolean usesDefaultGlowRenderer() {
		return true;
	}

}