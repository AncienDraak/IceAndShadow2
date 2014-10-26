package iceandshadow2.nyx.items;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import iceandshadow2.EnumIaSModule;
import iceandshadow2.ias.items.IaSBaseItemSingle;
import iceandshadow2.ias.items.IaSBaseItemSingleGlow;

public class NyxItemIngotEchir extends IaSBaseItemSingleGlow {

	@SideOnly(Side.CLIENT)
	protected IIcon active, invisible;

	public NyxItemIngotEchir(String texName) {
		super(EnumIaSModule.NYX, texName);
		this.setHasSubtypes(true);
		GameRegistry.addSmelting(this, new ItemStack(this,1,1), 0);
		GameRegistry.addShapelessRecipe(new ItemStack(this,1,0), new ItemStack(this,1,1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamageForRenderPass(int dmg, int pass) {
		if(dmg > 0)
			return active;
		if(pass == 1)
			return invisible;
		return this.itemIcon;
	}

	@Override
	public void addInformation(ItemStack s, EntityPlayer p,
			List l, boolean b) {
		if(s.getItemDamage() == 1) {
			l.add(	EnumChatFormatting.GRAY.toString()+
					EnumChatFormatting.ITALIC.toString()+
					"Sneak and Use Item to finalize.");
		} else {
			l.add(	EnumChatFormatting.GRAY.toString()+
					EnumChatFormatting.ITALIC.toString()+
					"This needs to be heated up...");
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister reg) {
		super.registerIcons(reg);
		active = reg.registerIcon(this.getTexName()+"Active");
		invisible = reg.registerIcon("IceAndShadow2:iasInvisible");
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if(stack.getItemDamage() == 1)
			return super.getUnlocalizedName()+"Active";
		return super.getUnlocalizedName();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack heap, World order,
			EntityPlayer pwai) {
		if(pwai.isSneaking())
			heap.setItemDamage(0);
		return heap;
	}
}
