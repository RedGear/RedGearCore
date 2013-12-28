package redgear.core.mod;

public class RedGear {
	
	
	public static final String CoreID = "RedGear|Core";
	public static final String CoreName = "Red Gear Core";
	public static final String CoreVersion = "1.5.8";
	public static final String CoreDepend = "";
	
	public static final String LocalePathPrefix = "/assets/";
	public static final String LocalePathSuffix = "/lang/";
	
	private static final String reqCore = "required-after:" + CoreID + ";";
	
	
	public static final String NetworkID = "RedGear|Network";
	public static final String NetworkName = "Network";
	public static final String NetworkVersion = "1.0.2";
	public static final String NetworkDepend = reqCore + "after:*";
	
	
	public static final String AdvancedEnergisticsID = "RedGear|AdvancedEnergistics";
	public static final String AdvancedEnergisticsName = "Advanced Energistics";
	public static final String AdvancedEnergisticsVersion = "1.0.0";
	public static final String AdvancedEnergisticsDepend = reqCore + "required-after:AppliedEnergistics";
	
	
	public static final String BrewcraftID = "RedGear|Brewcraft";
	public static final String BrewcraftName = "Brewcraft";
	public static final String BrewcraftVersion = "1.0.0";
	public static final String BrewcraftDepend = reqCore;
	
	
	public static final String GeocraftID = "RedGear|Geocraft";
	public static final String GeocraftName = "Geocraft";
	public static final String GeocraftVersion = "1.2.2";
	public static final String GeocraftDepend = reqCore;
	
	
	public static final String LiquidFuelsID = "RedGear|LiquidFuels";
	public static final String LiquidFuelsName = "Liquid Fuels";
	public static final String LiquidFuelsVersion = "2.2.0";
	public static final String LiquidFuelsDepend = reqCore + "after:Forestry; after:BuildCraft|Core";
	
	
	public static final String MoreBackpacksID = "RedGear|MoreBackpacks";
	public static final String MoreBackpacksName = "More Backpacks";
	public static final String MoreBackpacksVersion = "2.1.7";
	public static final String MoreBackpacksDepend = reqCore + "required-after:Forestry;";
	
	
	public static final String SecurecraftID = "RedGear|Securecraft";
	public static final String SecurecraftName = "Securecraft";
	public static final String SecurecraftVersion = "1.1.2";
	public static final String SecurecraftDepend = reqCore;
	
	
	public static final String SnowfallID = "RedGear|Snowfall";
	public static final String SnowfallName = "Snowfall";
	public static final String SnowfallVersion = "2.0.0";
	public static final String SnowfallDepend = reqCore;
	
	
	public static final String TileInterfaceID = "RedGear|TileInterface";
	public static final String TileInterfaceName = "Tile Interface";
	public static final String TileInterfaceVersion = "1.0.0";
	public static final String TileInterfaceDepend = reqCore;
}
