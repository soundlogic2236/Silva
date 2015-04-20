package soundlogic.silva.common.core.proxy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.ModBlocks;
import soundlogic.silva.common.block.tile.IForestClientTick;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.BifrostCreationHandler;
import soundlogic.silva.common.core.handler.BlockDropsHandler;
import soundlogic.silva.common.core.handler.ConfigHandler;
import soundlogic.silva.common.core.handler.DwarfForgedHandler;
import soundlogic.silva.common.core.handler.EnderPearlPortalHandler;
import soundlogic.silva.common.core.handler.PixieDustHandler;
import soundlogic.silva.common.core.handler.portal.DimensionHandler;
import soundlogic.silva.common.crafting.ModCraftingRecipes;
import soundlogic.silva.common.crafting.ModPortalTradeRecipes;
import soundlogic.silva.common.crafting.PortalRecipes;
import soundlogic.silva.common.entity.ModEntities;
import soundlogic.silva.common.item.ModItems;
import soundlogic.silva.common.lexicon.LexiconData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public static List<IForestClientTick> ForestWandRenderers = new ArrayList<IForestClientTick>();
	
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());

		Silva.creativeTab=new CreativeTabs("Silva") {
			
			@Override
			public Item getTabIconItem() {
				// TODO Auto-generated method stub
				return Items.apple;
			}
		};
		
		ModBlocks.preInit();
		ModItems.preInit();
		ModEntities.preInit();

		ModCraftingRecipes.preInit();
		PortalRecipes.preInit();
		ModPortalTradeRecipes.preInit();
		
		DimensionHandler.initSignatures();

		LexiconData.preInit();
	}

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new BifrostCreationHandler());
		MinecraftForge.EVENT_BUS.register(new PixieDustHandler());
		MinecraftForge.EVENT_BUS.register(new BlockDropsHandler());
		MinecraftForge.EVENT_BUS.register(new EnderPearlPortalHandler());
		DwarfForgedHandler dwarfForged=new DwarfForgedHandler();
		MinecraftForge.EVENT_BUS.register(dwarfForged);
		FMLCommonHandler.instance().bus().register(dwarfForged);
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		TilePortalCore.definePortalShape();

	}
	
	// Client stuff
    public void registerRenderers() {
            // Nothing here as the server doesn't render graphics or entities!
    }

	public int getTicks() {
        // Nothing here as the server doesn't render graphics or entities!
		return 0;
	}
}
