package redgear.core.tile

import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import scala.beans.BeanProperty

/**
 * @author Blackhole
 *         Created on 10/12/2014.
 */
trait Faced extends Savable{
  @BeanProperty
  var direction: ForgeDirection = ForgeDirection.SOUTH
  @BeanProperty
  var flat: Boolean = true
  @BeanProperty
  var allowUnknown: Boolean = false

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
      direction = (ForgeDirection.getOrientation(Faced.directionMap(MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3)))
    }
    else{
      var quadrant: Int = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3
      quadrant = if (entity.rotationPitch < -60.0F) 5 else if (entity.rotationPitch > 60.0F) 4 else quadrant
      direction = (ForgeDirection.getOrientation(Faced.directionMap(quadrant)))
    }


  }
  
   /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def writeToNBT(tag: NBTTagCompound){
    super.writeToNBT(tag)
    tag.setByte("direction", direction.ordinal().byteValue())
  }

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def readFromNBT(tag: NBTTagCompound){
    super.readFromNBT(tag)
    direction = if(tag.hasKey("direction"))
      ForgeDirection.getOrientation(tag.getByte("direction"))
    else
      ForgeDirection.SOUTH
  }
}

object Faced{
  private val directionMap = Array(2, 5, 3, 4, 1, 0)

   def setDirection(self: Faced, side: ForgeDirection) = {

      val fn = {if (!(self flat)) self direction = side}
      side match {
        case ForgeDirection.UNKNOWN => {if (self allowUnknown) self direction = side}
        case ForgeDirection.UP => fn
        case ForgeDirection.DOWN => fn
        case _ => self direction = side
      }
    }
}
