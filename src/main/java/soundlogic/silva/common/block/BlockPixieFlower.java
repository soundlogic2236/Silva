package soundlogic.silva.common.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.core.handler.BotaniaAccessHandler;
import soundlogic.silva.common.core.handler.ConfigHandler;
import soundlogic.silva.common.lexicon.LexiconData;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
public class BlockPixieFlower extends BlockFlower implements ILexiconable {

	Block flower;
	
	public BlockPixieFlower() {
		super(0);
		setHardness(0F);
		setStepSound(soundTypeGrass);
		setBlockBounds(0.3F, 0.0F, 0.3F, 0.8F, 1, 0.8F);
		setTickRandomly(false);
		setLightLevel(1F);
		setCreativeTab(Silva.creativeTab);
		flower = BotaniaAccessHandler.findBlock("flower");
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		float[] color = EntitySheep.fleeceColorTable[meta];

		if(par5Random.nextDouble() < BotaniaAccessHandler.BotaianConfig.flowerParticleFrequency) {
			BotaniaAccessHandler.sparkleFX(par1World, par2 + 0.3 + par5Random.nextFloat() * 0.5, par3 + 0.5 + par5Random.nextFloat() * 0.5, par4 + 0.3 + par5Random.nextFloat() * 0.5, color[0], color[1], color[2], par5Random.nextFloat(), 5);
			BotaniaAccessHandler.sparkleFX(par1World, par2 + 0.3 + par5Random.nextFloat() * 0.5, par3 + 0.5 + par5Random.nextFloat() * 0.5, par4 + 0.3 + par5Random.nextFloat() * 0.5, 1F, 0.25F, 0.9F, 0.1F + par5Random.nextFloat() * 0.25F, 12);
		}
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.pixie_farm;
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return flower.getIcon(par1, par2);
	}

}
