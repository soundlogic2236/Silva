package soundlogic.silva.common;

import net.minecraft.creativetab.CreativeTabs;
import soundlogic.silva.common.core.proxy.CommonProxy;
import soundlogic.silva.common.lib.LibMisc;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
public class Silva {

	public static CreativeTabs creativeTab;
	
    // The instance of your mod that Forge uses.
    @Instance(value = LibMisc.MOD_ID)
    public static Silva instance;
    
    // Says where the client and server 'proxy' code is loaded.
	@SidedProxy(serverSide = LibMisc.PROXY_COMMON, clientSide = LibMisc.PROXY_CLIENT)
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
            proxy.registerRenderers();
    }
    
}
