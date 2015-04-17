package soundlogic.silva.common.lib;

public class LibMisc {

	// Mod Constants
	public static final String MOD_ID = "Silva";
	public static final String MOD_NAME = MOD_ID;
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-after:Forge@[10.13.2.1291,);required-after:Baubles;required-after:Botania;after:Thaumcraft";
	
	// Proxy Constants
	public static final String PROXY_COMMON = "soundlogic.silva.common.core.proxy.CommonProxy";
	public static final String PROXY_CLIENT = "soundlogic.silva.client.core.proxy.ClientProxy";

}
