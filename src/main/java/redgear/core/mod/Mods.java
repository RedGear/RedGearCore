package redgear.core.mod;

import cpw.mods.fml.common.Loader;

public enum Mods
{
	Geocraft("RedGear|Geocraft", "Geocraft"),
	Snowfall("RedGear|Snowfall", "Snowfall"),
	
    IC2("IC2", "Industrialcraft 2"),
    Greg("GregTech_Addon", "GregTech"),
    Thaum("Thaumcraft"),
    MetallurgyBase("Metallurgy3Base", "Metallurgy Base"),
    MetallurgyPrecious("MetallurgyPrecious", "Metallurgy Precious"),
    MetallurgyUtility("MetallurgyUtility", "Metallurgy Utility"),
    ComputerCraft("ComputerCraft"),
    BuildcraftCore("BuildCraft|Core", "Buildcraft"),
    StevesCarts("StevesCarts", "Steve's Carts"),
    Railcraft("Railcraft"),
    ThermalExpansion("ThermalExpansion", "Thermal Expansion"),
	Forestry("Forestry"),
	TConstruct("TConstruct", "Tinker's Construct"),
	Natura("Natura"),
	BiomesOPlenty("BiomesOPlenty", "Biomes O' Plenty"),
	NetherOres("NetherOres", "Nether Ores"),
	CoFHCore("CoFHCore");

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
