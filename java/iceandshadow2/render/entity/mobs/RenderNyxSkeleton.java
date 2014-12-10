package iceandshadow2.render.entity.mobs;

import iceandshadow2.nyx.entities.mobs.EntityNyxSkeleton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNyxSkeleton extends RenderBiped {
	private static ResourceLocation winterskeletonskin = new ResourceLocation(
			"iceandshadow2:textures/mob/winterskeleton.png");
	private static ResourceLocation necromancerskin = new ResourceLocation(
			"iceandshadow2:textures/mob/wickednecromancer.png");
	
	private static ResourceLocation winterskeleton_eyes = new ResourceLocation(
			"iceandshadow2:textures/mob/winterskeleton_eyes.png");
	private static ResourceLocation wickednecromancer_eyes = new ResourceLocation(
			"iceandshadow2:textures/mob/wickednecromancer_eyes.png");

	public RenderNyxSkeleton() {
		super(new ModelSkeleton(), 0.5F);
		this.renderPassModel = new ModelSkeleton();
	}

	protected void func_82438_a(EntitySkeleton par1EntitySkeleton, float par2) {
		if (par1EntitySkeleton.getSkeletonType() == 1) {
			GL11.glScalef(1.2F, 1.2F, 1.2F);
		}
	}
	
    /**
     * Queries whether should render the specified pass or not.
     */ 
    @Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
    	this.setSkeletonEyeBrightness((EntityNyxSkeleton)par1EntityLivingBase, par2, par3);
    	return super.shouldRenderPass(par1EntityLivingBase, par2, par3);
    }
    
	/**
	 * Sets the skeleton's glowing eyes
	 */
	protected void setSkeletonEyeBrightness(EntityNyxSkeleton par1Entity,
			int par2, float par3) {
		if (par2 > 1 || par1Entity.isInvisible() || 
				par1Entity.hurtTime > 0 || par1Entity.getHealth() <= 0.0F)
			return;
		else {
			float f1 = 1.0F;

			if(par1Entity.getSkeletonType() == 1)
				this.bindTexture(wickednecromancer_eyes);
			else
				this.bindTexture(winterskeleton_eyes);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(true);

            char c0 = 61680;
            int j = c0 % 65536;
            int k = c0 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            this.modelBipedMain.bipedHead.renderWithRotation(par3);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

	@Override
	protected void func_82422_c() {
		GL11.glTranslatef(0.09375F, 0.1875F, 0.0F);
	}

	protected ResourceLocation func_110860_a(
			EntityNyxSkeleton par1EntitySkeleton) {
		return par1EntitySkeleton.getSkeletonType() == 1 ? necromancerskin
				: winterskeletonskin;
	}

	protected ResourceLocation func_110856_a(EntityLiving par1EntityLiving) {
		return this.func_110860_a((EntityNyxSkeleton) par1EntityLiving);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase,
			float par2) {
		this.func_82438_a((EntitySkeleton) par1EntityLivingBase, par2);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.func_110860_a((EntityNyxSkeleton) par1Entity);
	}
}
