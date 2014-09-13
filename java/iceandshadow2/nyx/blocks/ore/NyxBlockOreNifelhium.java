package iceandshadow2.nyx.blocks.ore;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import iceandshadow2.ias.blocks.IaSBaseBlockMulti;
import iceandshadow2.ias.blocks.IaSBaseBlockSingle;
import iceandshadow2.nyx.NyxItems;
import iceandshadow2.nyx.blocks.NyxBlockStone;
import iceandshadow2.util.EnumIaSModule;

public class NyxBlockOreNifelhium extends NyxBlockOre {

	public NyxBlockOreNifelhium(String texName) {
		super(texName);
		this.setHarvestLevel("pickaxe", 2);
		this.setHardness(20.0F);
		this.setLuminescence(0.3F);
		this.setLightColor(0.9F, 0.9F, 0.9F);
		this.setResistance(15.0F);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune) {
		ArrayList<ItemStack> is = new ArrayList<ItemStack>();
		is.add(new ItemStack(NyxItems.nifelhiumPowder,1+(world.rand.nextInt(1+fortune)>=2?1:0)));
		return is;
	}

	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		return 7;
	}
}
