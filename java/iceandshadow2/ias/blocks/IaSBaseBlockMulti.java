package iceandshadow2.ias.blocks;

import iceandshadow2.IceAndShadow2;
import iceandshadow2.ias.IIaSModName;
import iceandshadow2.util.EnumIaSModule;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public abstract class IaSBaseBlockMulti extends IaSBaseBlock implements IIaSModName {
	
	protected IIcon[] icons;
	public final byte subtypeCount;
	
	public IaSBaseBlockMulti(EnumIaSModule mod, String id, Material mat, byte subtypes) {
		super(mod, mat);
		this.setBlockName(mod.prefix+id);
		subtypeCount = subtypes;
	}
	
	@Override
	public String getModName() {
		return this.getUnlocalizedName().substring(5);
	}
	
	@Override
	public String getTexName() {
		return IceAndShadow2.MODID+':'+MODULE.prefix+getModName();
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		icons = new IIcon[subtypeCount];
		for(byte i = 0; i < subtypeCount; ++i)
			reg.registerIcon(getTexName()+i);
	}
	
	public boolean getHasSubtypes() {
		return true;
	}
	
    public String getUnlocalizedName(int val) {
		return super.getUnlocalizedName()+val;
    }

}