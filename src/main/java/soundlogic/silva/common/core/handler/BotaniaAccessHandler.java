package soundlogic.silva.common.core.handler;

import org.lwjgl.opengl.ARBShaderObjects;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;

public class BotaniaAccessHandler {

	static ShaderCallback dopplegangerCallBack = new ShaderCallback() {

		@Override
		public void call(int shader) {
			// Frag Uniforms
			int disfigurationUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "disfiguration");
			ARBShaderObjects.glUniform1fARB(disfigurationUniform, 0.025F);

			// Vert Uniforms
			int grainIntensityUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "grainIntensity");
			ARBShaderObjects.glUniform1fARB(grainIntensityUniform, 0.05F);
		}
	};	
	
	public static vazkii.botania.client.lib.LibResources LibResources;
	public static vazkii.botania.common.core.handler.ConfigHandler BotaianConfig;

	public static void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {
		Botania.proxy.sparkleFX(world, x, y, z, r, g, b, size, m, fake);
	}
	public static void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		Botania.proxy.sparkleFX(world, x, y, z, r, g, b, size, m);
	}
	public static void lightningFX(World world, Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, int colorOuter, int colorInner) {
		Botania.proxy.lightningFX(world, vectorStart, vectorEnd, ticksPerMeter, colorOuter, colorInner);
	}
	public static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity, float ageMaxMul) {
		Botania.proxy.wispFX(world, x, y, z, r, g, b, size, gravity, ageMaxMul);
	}
	public static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity) {
		Botania.proxy.wispFX(world, x, y, z, r, g, b, size, gravity);
	}
	public static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionX, float motionY, float motionZ) {
		Botania.proxy.wispFX(world, x, y, z, r, g, b, size, motionX, motionY, motionZ);
	}
	public static void setWispFXDepthTest(boolean depth) {
		Botania.proxy.setWispFXDepthTest(depth);
	}
	public static Block findBlock(String name) {
		Block block = GameRegistry.findBlock("Botania", name);
		if(block==null)
			throw new NullPointerException("Could not find botania block named "+name);
		return block;
	}
	public static Item findItem(String name) {
		Item item = GameRegistry.findItem("Botania", name);
		if(item==null)
			throw new NullPointerException("Could not find botania item named "+name);
		return item;
	}
	public static void startDopplegangerRendering() {
		ShaderHelper.useShader(ShaderHelper.doppleganger, dopplegangerCallBack);
	}
	public static void endDopplegangerRendering() {
		ShaderHelper.releaseShader();
	}
}
