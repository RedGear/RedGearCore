package redgear.core.api.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

public interface IWrenchableTile {
	
	/**
	 * Called when a player right clicks this tile while holding a Buildcraft
	 * wrench
	 *
	 * @param player The Player who clicked.
	 * @param side The Side the player clicked on.
	 * @return true if the gui should open, false if it should not.
	 */
	boolean wrenched(EntityPlayer player, ForgeDirection side);

	/**
	 * Called when a player right clicks this tile while holding a Buildcraft
	 * wrench and holding shift
	 *
	 * @param player The Player who clicked.
	 * @param side The Side the player clicked on
	 * @return true if the gui should open, false if it should not.
	 */
	boolean wrenchedShift(EntityPlayer player, ForgeDirection side);

}
