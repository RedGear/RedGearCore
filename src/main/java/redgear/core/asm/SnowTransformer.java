package redgear.core.asm;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.RETURN;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class SnowTransformer implements IClassTransformer
{
    private static String worldName = "Lnet/minecraft/world/World;";
    private static String playerName = "Lnet/minecraft/entity/player/EntityPlayer;";
    private static String itemStackName = "Lnet/minecraft/item/ItemStack;";
    private static String snowfallHooks = "redgear/core/asm/SnowfallHooks";

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (transformedName.equals("net.minecraft.world.World") && RedGearCoreLoadingPlugin.util.getBoolean("SnowOnIce"))
        {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList canSnowAtBody = new InsnList();
            canSnowAtBody.add(new VarInsnNode(ALOAD, 0));
            canSnowAtBody.add(new VarInsnNode(ILOAD, 1));
            canSnowAtBody.add(new VarInsnNode(ILOAD, 2));
            canSnowAtBody.add(new VarInsnNode(ILOAD, 3));
            canSnowAtBody.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "canSnowAtBody", "(" + worldName + "III)Z"));
            canSnowAtBody.add(new InsnNode(IRETURN));

            for (MethodNode mn : node.methods)
                if (mn.name.equals("canSnowAtBody"))
                    mn.instructions = canSnowAtBody;

            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }

        if (transformedName.equals("net.minecraft.item.ItemSnow") && RedGearCoreLoadingPlugin.util.getBoolean("SnowLayerStackFix"))
        {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList onItemUse = new InsnList();
            LabelNode skip29 = new LabelNode();
            LabelNode skip38 = new LabelNode();
            onItemUse.add(new VarInsnNode(ALOAD, 1));
            onItemUse.add(new VarInsnNode(ALOAD, 2));
            onItemUse.add(new VarInsnNode(ALOAD, 3));
            onItemUse.add(new VarInsnNode(ILOAD, 4));
            onItemUse.add(new VarInsnNode(ILOAD, 5));
            onItemUse.add(new VarInsnNode(ILOAD, 6));
            onItemUse.add(new VarInsnNode(ILOAD, 7));
            onItemUse.add(new VarInsnNode(FLOAD, 8));
            onItemUse.add(new VarInsnNode(FLOAD, 9));
            onItemUse.add(new VarInsnNode(FLOAD, 10));
            onItemUse.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "onItemUse", "(" + itemStackName + playerName + worldName + "IIIIFFF)I"));
            onItemUse.add(new VarInsnNode(ISTORE, 11));
            onItemUse.add(new VarInsnNode(ILOAD, 11));
            onItemUse.add(new JumpInsnNode(IFNE, skip29));
            onItemUse.add(new InsnNode(ICONST_1));
            onItemUse.add(new InsnNode(IRETURN));
            onItemUse.add(skip29);
            onItemUse.add(new VarInsnNode(ILOAD, 11));
            onItemUse.add(new InsnNode(ICONST_2));
            onItemUse.add(new JumpInsnNode(IF_ICMPNE, skip38));
            onItemUse.add(new IincInsnNode(5, 1));
            onItemUse.add(skip38);
            onItemUse.add(new VarInsnNode(ALOAD, 0));
            onItemUse.add(new VarInsnNode(ALOAD, 1));
            onItemUse.add(new VarInsnNode(ALOAD, 2));
            onItemUse.add(new VarInsnNode(ALOAD, 3));
            onItemUse.add(new VarInsnNode(ILOAD, 4));
            onItemUse.add(new VarInsnNode(ILOAD, 5));
            onItemUse.add(new VarInsnNode(ILOAD, 6));
            onItemUse.add(new VarInsnNode(ILOAD, 7));
            onItemUse.add(new VarInsnNode(FLOAD, 8));
            onItemUse.add(new VarInsnNode(FLOAD, 9));
            onItemUse.add(new VarInsnNode(FLOAD, 10));
            onItemUse.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/item/ItemBlock", "func_77648_a", "(" + itemStackName + playerName + worldName + "IIIIFFF)Z"));
            onItemUse.add(new InsnNode(IRETURN));

            for (MethodNode method : node.methods)
                if ("func_77648_a".equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc)))
                	method.instructions = onItemUse;

            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }

        if (transformedName.equals("net.minecraft.block.BlockSnowBlock") && RedGearCoreLoadingPlugin.util.getBoolean("SnowfallShovelHook"))
        {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList harvestBlock = new InsnList();
            LabelNode skip = new LabelNode();
            harvestBlock.add(new VarInsnNode(ALOAD, 1));
            harvestBlock.add(new VarInsnNode(ALOAD, 2));
            harvestBlock.add(new VarInsnNode(ILOAD, 3));
            harvestBlock.add(new VarInsnNode(ILOAD, 4));
            harvestBlock.add(new VarInsnNode(ILOAD, 5));
            harvestBlock.add(new VarInsnNode(ILOAD, 6));
            harvestBlock.add(new InsnNode(ICONST_0));
            harvestBlock.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "snowShovelHook", "(" + worldName + playerName + "IIIIZ)Z"));
            harvestBlock.add(new JumpInsnNode(IFEQ, skip));
            harvestBlock.add(new InsnNode(RETURN));
            harvestBlock.add(skip);
            harvestBlock.add(new VarInsnNode(ALOAD, 0));
            harvestBlock.add(new VarInsnNode(ALOAD, 1));
            harvestBlock.add(new VarInsnNode(ALOAD, 2));
            harvestBlock.add(new VarInsnNode(ILOAD, 3));
            harvestBlock.add(new VarInsnNode(ILOAD, 4));
            harvestBlock.add(new VarInsnNode(ILOAD, 5));
            harvestBlock.add(new VarInsnNode(ILOAD, 6));
            harvestBlock.add(new MethodInsnNode(INVOKESPECIAL, node.superName, "harvestBlock", "(" + worldName + playerName + "IIII)V"));
            harvestBlock.add(new VarInsnNode(ALOAD, 1));
            harvestBlock.add(new VarInsnNode(ILOAD, 3));
            harvestBlock.add(new VarInsnNode(ILOAD, 4));
            harvestBlock.add(new VarInsnNode(ILOAD, 5));
            harvestBlock.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", "setBlockToAir", "(III)Z"));
            harvestBlock.add(new InsnNode(POP));
            harvestBlock.add(new InsnNode(RETURN));
            MethodNode mn = new MethodNode(ACC_PUBLIC, "harvestBlock", "(" + worldName + playerName + "IIII)V", null, new String[] {});
            mn.instructions = harvestBlock;
            node.methods.add(mn);
            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }

        if (transformedName.equals("net.minecraft.block.BlockSnow") && (RedGearCoreLoadingPlugin.util.getBoolean("SnowGrowthAndDecay") || RedGearCoreLoadingPlugin.util.getBoolean("SnowPlaceOnSolidSide") || RedGearCoreLoadingPlugin.util.getBoolean("SnowfallShovelHook")))
        {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList updateTick = new InsnList();
            updateTick.add(new VarInsnNode(ALOAD, 1));
            updateTick.add(new VarInsnNode(ILOAD, 2));
            updateTick.add(new VarInsnNode(ILOAD, 3));
            updateTick.add(new VarInsnNode(ILOAD, 4));
            updateTick.add(new VarInsnNode(ALOAD, 5));
            updateTick.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "updateTick", "(" + worldName + "IIILjava/util/Random;)V")); // call the updateTick method from the SnowTransformer class
            updateTick.add(new InsnNode(RETURN));
            InsnList harvestBlock = new InsnList();
            LabelNode skip = new LabelNode();
            harvestBlock.add(new VarInsnNode(ALOAD, 1));
            harvestBlock.add(new VarInsnNode(ALOAD, 2));
            harvestBlock.add(new VarInsnNode(ILOAD, 3));
            harvestBlock.add(new VarInsnNode(ILOAD, 4));
            harvestBlock.add(new VarInsnNode(ILOAD, 5));
            harvestBlock.add(new VarInsnNode(ILOAD, 6));
            harvestBlock.add(new InsnNode(ICONST_1));
            harvestBlock.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "snowShovelHook", "(" + worldName + playerName + "IIIIZ)Z"));
            harvestBlock.add(new JumpInsnNode(IFEQ, skip));
            harvestBlock.add(new InsnNode(RETURN));
            harvestBlock.add(skip);
            InsnList canPlaceBlockAt = new InsnList();
            canPlaceBlockAt.add(new VarInsnNode(ALOAD, 1));
            canPlaceBlockAt.add(new VarInsnNode(ILOAD, 2));
            canPlaceBlockAt.add(new VarInsnNode(ILOAD, 3));
            canPlaceBlockAt.add(new VarInsnNode(ILOAD, 4));
            canPlaceBlockAt.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "canPlaceBlockAt", "(" + worldName + "III)Z"));
            canPlaceBlockAt.add(new InsnNode(IRETURN));
            InsnList isBlockSolidOnSide = new InsnList();
            isBlockSolidOnSide.add(new VarInsnNode(ALOAD, 1));
            isBlockSolidOnSide.add(new VarInsnNode(ILOAD, 2));
            isBlockSolidOnSide.add(new VarInsnNode(ILOAD, 3));
            isBlockSolidOnSide.add(new VarInsnNode(ILOAD, 4));
            isBlockSolidOnSide.add(new VarInsnNode(ALOAD, 5));
            isBlockSolidOnSide.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "isBlockSolidOnSide", "(" + worldName + "IIILnet/minecraftforge/common/ForgeDirection;)Z"));
            isBlockSolidOnSide.add(new InsnNode(IRETURN));
            MethodNode isBlockSolid = new MethodNode(ACC_PUBLIC, "isBlockSolidOnSide", "(" + worldName + "IIILnet/minecraftforge/common/ForgeDirection;)Z", null, new String[] {});
            isBlockSolid.instructions = isBlockSolidOnSide;
            node.methods.add(isBlockSolid);
            /*InsnList isBlockReplaceable = new InsnList();
            isBlockReplaceable.add(new InsnNode(ICONST_1));
            isBlockReplaceable.add(new InsnNode(IRETURN));
            MethodNode replaceable = new MethodNode(ACC_PUBLIC, "isBlockReplaceable", "(" + worldName + "III)Z", null, new String[] {});
            replaceable.instructions = isBlockReplaceable;
            node.methods.add(replaceable);*/

            for (MethodNode method : node.methods){
            	String mappedName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc);
                if ("func_71847_b".equals(mappedName) && RedGearCoreLoadingPlugin.util.getBoolean("SnowGrowthAndDecay"))
                	method.instructions = updateTick;

                if ("func_71893_a".equals(mappedName) && RedGearCoreLoadingPlugin.util.getBoolean("SnowfallShovelHook"))
                	method.instructions.insertBefore(method.instructions.getFirst(), harvestBlock);

                if ("func_71930_b".equals(mappedName) && RedGearCoreLoadingPlugin.util.getBoolean("SnowPlaceOnSolidSide"))
                	method.instructions = canPlaceBlockAt;
            }

            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }

        return bytes;
    }
}
