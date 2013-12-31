package redgear.core.asm;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.compat.Mods;

public class SnowfallHooks
{
    public static void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int j1 = par1World.getBlockMetadata(par2, par3, par4);
        int k1 = j1 & 7;

        if (par1World.getSavedLightValue(EnumSkyBlock.Block, par2, par3, par4) > 11)
            if (j1 < 1)
            {
                par1World.setBlock(par2, par3, par4, 0, 0, 2);
            }
            else
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, k1 - 1, 2);
            }
        else if (j1 < 7 && par1World.isRaining() && par5Random.nextInt(8) + 1 > 7 && par1World.canBlockSeeTheSky(par2, par3, par4))
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, k1 + 1 | j1 & -8, 2);
        }
    }

    public static boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        int l = world.getBlockId(x, y - 1, z);
        Block block = Block.blocksList[l];

        if (block == null)
            return false;
        
        return block.isLeaves(world, x, y, z) || world.isBlockSolidOnSide(x, y - 1, z, ForgeDirection.UP);
    }

    public static boolean canSnowAtBody(World world, int x, int y, int z)
    {
        BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x, z);
        float f = biomegenbase.getFloatTemperature();

        if (f > 0.15F)
        {
            return false;
        }
        else
        {
            if (y >= 0 && y < 256 && world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < 10)
            {
                int i1 = world.getBlockId(x, y, z);

                if (i1 == 0 && Block.snow.canPlaceBlockAt(world, x, y, z))
                {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection direct){
    	return direct != ForgeDirection.UP || world.getBlockMetadata(x, y, z) > 6;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public static int onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par1ItemStack.stackSize == 0)
            return 1;

        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
            return 1;

        int i1 = par3World.getBlockId(par4, par5, par6);

        if (i1 == Block.snow.blockID){
            Block block = Block.snow;
            int meta = par3World.getBlockMetadata(par4, par5, par6);

            if (meta <= 6)
            {
                if (par3World.checkNoEntityCollision(block.getCollisionBoundingBoxFromPool(par3World, par4, par5, par6)) && par3World.setBlockMetadataWithNotify(par4, par5, par6, meta + 1, 2))
                {
                    par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                    --par1ItemStack.stackSize;

                    return 0;
                }
            }
            else
                return 2;
        }

        return 1;
    }

    /**
     *
     * @param world
     * @param player
     * @param x
     * @param y
     * @param z
     * @param meta
     * @return true if the player was holding a snowshovel, false otherwise
     */
    public static boolean snowShovelHook(World world, EntityPlayer player, int x, int y, int z, int meta, boolean isLayered)
    {
        ItemStack heldItem = player.getHeldItem();

        if (Mods.Snowfall.isIn() && heldItem != null && heldItem.getItem() instanceof ISnowShovel && !world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = null;

            if (isLayered)
            {
                entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, new ItemStack(Block.snow, meta + 1, 0));
            }
            else
            {
                entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, new ItemStack(Block.blockSnow, 1, 0));
            }

            if (entityitem != null){
                entityitem.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityitem);
            }

            world.setBlockToAir(x, y, z);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public interface ISnowShovel{
    	
    }
}
