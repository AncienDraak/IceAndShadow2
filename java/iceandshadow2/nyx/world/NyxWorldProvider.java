package iceandshadow2.nyx.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

public class NyxWorldProvider extends WorldProvider {

	public static final Vec3 NYX_COLOR = Vec3.fakePool.getVecFromPool(0.0, 0.0, 0.05F);
	
	public NyxWorldProvider() {
		this.isHellWorld = false;
		this.hasNoSky = true;
	}
	
	@Override
	public float calculateCelestialAngle(long par1, float par3) {
		return 0.5F;
	}

	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
		return true;
	}

	@Override
	public boolean canDoLightning(Chunk chunk) {
		return false;
	}

	@Override
	public boolean canDoRainSnowIce(Chunk chunk) {
		return false;
	}

	@Override
	public boolean canRespawnHere() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int par1, int par2) {
		return true;
	}

	@Override
	protected void generateLightBrightnessTable() {
		float f = 0.005F;
        for (int i = 0; i <= 15; ++i) {
            float var3 = 1.0F - (float)(i) / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - var3) / (var3 * 2.0F + 2.0F) * (1.0F - f) + f;
        }
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		return 176;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float par1, float par2) {
		return NYX_COLOR;
	}

	@Override
	public int getMoonPhase(long par1) {
		return 4;
	}

	@Override
	public double getMovementFactor() {
		return 3.0;
	}

	@Override
	public String getSaveFolder() {
		return "DIM-IaS-nyx";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
		return NYX_COLOR;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		return super.getSkyRenderer();
	}

	@Override
	public String getWelcomeMessage() {
		return "Entering Nyx...";
	}

	@Override
	public String getDepartMessage() {
		return "Escaping Nyx...";
	}

	@Override
	public boolean isDaytime() {
		return false;
	}

	@Override
	public boolean isSurfaceWorld() {
		return true;
	}

	@Override
	public String getDimensionName() {
		return "Nyx";
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		// TODO Auto-generated method stub
		return super.createChunkGenerator();
	}
}
