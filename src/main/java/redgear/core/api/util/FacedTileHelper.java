package redgear.core.api.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class FacedTileHelper {

	private static final int[] directionMap = {2, 5, 3, 4, 1, 0};
	
	public static ForgeDirection facePlayerFlat(EntityLivingBase entity){
		return ForgeDirection.getOrientation(directionMap[MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3]);
	}
	
	public static ForgeDirection facePlayer(EntityLivingBase entity){
		int quadrant = MathHelper.floor_double(entity.rotationPitch * 4.0F / 360.0F + 0.5D) & 0x3;
		quadrant = entity.rotationYaw < -60.0F ? 5 : entity.rotationYaw > 60.0F ? 4 : quadrant;
		
		return ForgeDirection.getOrientation(directionMap[quadrant]);
	}
}
