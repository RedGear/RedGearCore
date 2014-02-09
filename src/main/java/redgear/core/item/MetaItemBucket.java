package redgear.core.item;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import redgear.core.util.SimpleItem;
import redgear.core.world.Location;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MetaItemBucket extends MetaItem {

	public MetaItemBucket(String name) {
		super(name);
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabMisc);
		setContainerItem(Items.bucket);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public SimpleItem addMetaItem(SubItemBucket newItem) {
		SimpleItem temp = super.addMetaItem(newItem);
		FluidContainerRegistry.registerFluidContainer(newItem.fluid, temp.getStack(),
				FluidContainerRegistry.EMPTY_BUCKET);
		return temp;
	}

	@Override
	public SimpleItem addMetaItem(SubItem newItem) {
		if (!(newItem instanceof SubItemBucket))
			throw new ClassCastException("MetaBucketContainer can only except MetaBucket!");
		return addMetaItem((SubItemBucket) newItem);
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		Location loc = new Location(event.target);

		Block id = loc.getBlock(event.world);
		int meta = getMeta(id);

		if (meta > -1)
			if (loc.getBlockMeta(event.world) == 0) { // Check that it is a source block
				loc.setAir(event.world);
				; // Remove the fluid block

				event.result = new ItemStack(this, 1, meta);
				event.setResult(Result.ALLOW);
			}
	}

	private int getMeta(Block block) {
		for (Entry<Integer, SubItem> set : items.entrySet())
			if (((SubItemBucket) set.getValue()).fluid.getBlock().equals(block))
				return set.getKey();
		return -1;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer,
				false);

		if (movingobjectposition == null)
			return par1ItemStack;
		else {
			FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, par2World,
					movingobjectposition);
			if (MinecraftForge.EVENT_BUS.post(event))
				return par1ItemStack;

			if (event.getResult() == Event.Result.ALLOW) {
				if (par3EntityPlayer.capabilities.isCreativeMode)
					return par1ItemStack;

				if (--par1ItemStack.stackSize <= 0)
					return event.result;

				if (!par3EntityPlayer.inventory.addItemStackToInventory(event.result))
					par3EntityPlayer.entityDropItem(event.result, 0);

				return par1ItemStack;
			}

			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
					return par1ItemStack;

				if (movingobjectposition.sideHit == 0)
					--j;

				if (movingobjectposition.sideHit == 1)
					++j;

				if (movingobjectposition.sideHit == 2)
					--k;

				if (movingobjectposition.sideHit == 3)
					++k;

				if (movingobjectposition.sideHit == 4)
					--i;

				if (movingobjectposition.sideHit == 5)
					++i;

				if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
					return par1ItemStack;

				if (tryPlaceContainedLiquid(par2World, par1ItemStack, i, j, k)
						&& !par3EntityPlayer.capabilities.isCreativeMode)
					return new ItemStack(Items.bucket);
			}
			return par1ItemStack;
		}
	}

	/**
	 * Attempts to place the liquid contained inside the bucket.
	 */
	public boolean tryPlaceContainedLiquid(World world, ItemStack bucket, int x, int y, int z) {
		Location loc = new Location(x, y, z);

		Material material = loc.getMaterial(world);
		boolean flag = !material.isSolid();

		if (!loc.isAir(world) && !flag)
			return false;
		else {
			if (!world.isRemote && flag && !material.isLiquid())
				loc.setAir(world);

			Fluid working = ((SubItemBucket) getMetaItem(bucket.getItemDamage())).fluid;

			if (working == null || working.getBlock() == null)
				return false;
			else
				loc.placeBlock(world, working.getBlock());
			return true;
		}
	}
}
