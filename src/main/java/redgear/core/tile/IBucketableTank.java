package redgear.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IBucketableTank {

	boolean bucket(EntityPlayer player, int index, ItemStack container);

	boolean fill(EntityPlayer player, int index, ItemStack container);

	boolean empty(EntityPlayer player, int index, ItemStack container);
}
