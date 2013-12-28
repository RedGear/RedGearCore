package redgear.core.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import redgear.core.tile.TileEntitySmart;
/**
 * Block designed to be used with TileEntitySmart which together save the name of the person who placed them
 * @author Blackhole
 *
 */
public class BlockSmart extends BlockGeneric implements ITileEntityProvider
{
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        try
        {
            TileEntitySmart myTile = (TileEntitySmart) world.getBlockTileEntity(x, y, z);
            myTile.ownerName = ((EntityPlayer) entity).username;
        }
        catch (Exception e) {} //if the tile or entity don't cast right, just give up.
    }

    public String getOwner(World world, int x, int y, int z)
    {
        try
        {
            TileEntitySmart myTile = ((TileEntitySmart) world.getBlockTileEntity(x, y, z));
            return myTile.ownerName;
        }
        catch (Exception e)  //is something goes wrong, just return an empty string
        {
            return "";
        }
    }

    /**
     * Override this if you want to use a child of TileEntitySmart
     */
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntitySmart();
    }

    /**
     * Use this if your item has a typical icon
     * @param Id The typical ItemId
     * @param name Name of item's icon
     */
    public BlockSmart(int Id, Material material, String name)
    {
        super(Id, material, name);
    }
}
