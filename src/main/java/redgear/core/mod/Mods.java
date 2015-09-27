package redgear.core.mod;

import cpw.mods.fml.common.Loader;

public enum Mods
{
	Core("redgear_core", "Red Gear Core"),
	Brewcraft("redgear_brewcraft", "Brewcraft"),
	Geocraft("redgear_geocraft", "Geocraft"),
	LiquidFuels("redgear_liquidfuels", "Liquid Fuels"),
	MoreBackpacks("redgear_morebackpacks", "More Backpacks"),
	Securercraft("redgear_securecraft", "Securecraft"),
	Snowfall("redgear_snowfall", "Snowfall"),
	TileInterface("redgear_tileinterface", "Tile Interface"),
	
    IC2("IC2", "Industrialcraft 2"),
    Greg("GregTech_Addon", "GregTech"),
    
    Thaum("Thaumcraft"),
    
    MetallurgyBase("Metallurgy3Base", "Metallurgy Base"),
    MetallurgyPrecious("MetallurgyPrecious", "Metallurgy Precious"),
    MetallurgyUtility("MetallurgyUtility", "Metallurgy Utility"),

    UndergroundBiomes("UndergroundBiomes", "Underground Biomes Constructs"),
    
    ComputerCraft("ComputerCraft"),
    
    BCCore("BuildCraft|Core", "Buildcraft"),
    BCBuilders("BuildCraft|Builders", "Buildcraft Builders"),
    BCEnergy("BuildCraft|Energy", "Buildcraft Energy"),
    BCFactory("BuildCraft|Factory", "Buildcraft Factory"),
    BCTransport("BuildCraft|Transport", "Buildcraft Transport"),
    BCSilicon("BuildCraft|Silicon", "Buildcraft Silicon"),
    
    StevesCarts("StevesCarts", "Steve's Carts"),
    
    Railcraft("Railcraft"),
    
	CoFHCore("CoFHCore"),
    ThermalExpansion("ThermalExpansion", "Thermal Expansion"),
    
	Forestry("Forestry"),
	
	TConstruct("TConstruct", "Tinker's Construct"),
	Natura("Natura"),
	
	BiomesOPlenty("BiomesOPlenty", "Biomes O' Plenty"),
	
	NetherOres("NetherOres", "Nether Ores"),

    ForgeMulitPart("ForgeMultipart"),
    ForgeMicroBlock("ForgeMicroblock"),

    AE2("appliedenergistics2", "Applied Energistics 2"),
	TSteelworks("TSteelworks", "Tinker's Steelworks"),
	NEI("NotEnoughItems");

    private String ModId; //System Mod ID
    private String ModName; //User readable name

    Mods(String Id, String Name)
    {
        ModId = Id;
        ModName = Name;
    }

    Mods(String Id)
    {
        this(Id, Id);   //Use this if the Id and Name should be the same
    }

    public String getId()
    {
        return ModId;
    }
    public String getName()
    {
        return ModName;
    }
    
    public boolean isIn(){
    	return Loader.isModLoaded(ModId);
    }
}
