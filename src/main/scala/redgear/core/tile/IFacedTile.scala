package redgear.core.tile

import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

/**
 * @author Blackhole
 *         Created on 10/12/2014.
 */
trait IFacedTile {
  //private var _direction: ForgeDirection = ForgeDirection.SOUTH
  //var flat: Boolean = true
  //var allowUnknown: Boolean = false

  def flat: Boolean

  def allowUnknown: Boolean

  def getDirection: ForgeDirection

  def setDirection(side: ForgeDirection)

  /**
   * Blocks should call this to allow the Tile to decide how to face itself.
   * @param world
   * @param x
   * @param y
   * @param z
   * @param entity
   * @param stack
   */
  def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, entity: EntityLivingBase, stack: ItemStack) = {
    if(flat){
      setDirection(ForgeDirection.getOrientation(IFacedTile.directionMap(MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3)))
    }
    else{
      var quadrant: Int = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3
      quadrant = if (entity.rotationPitch < -60.0F) 5 else if (entity.rotationPitch > 60.0F) 4 else quadrant
      setDirection(ForgeDirection.getOrientation(IFacedTile.directionMap(quadrant)))
    }


  }
}

object IFacedTile{
  private val directionMap: Array[Int] = Array(2, 5, 3, 4, 1, 0)

   def setDirection(self: IFacedTile, side: ForgeDirection) = {

      val fn = {if (!(self flat)) self setDirection side}
      side match {
        case ForgeDirection.UNKNOWN => {if (self allowUnknown) self setDirection side}
        case ForgeDirection.UP => fn
        case ForgeDirection.DOWN => fn
        case _ => self setDirection side
      }
    }
}
