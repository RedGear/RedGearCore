package redgear.core.asm;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IRETURN;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class GlassAndIceTransformer implements IClassTransformer 
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes){
        if ((transformedName.equals("net.minecraft.block.BlockGlass") && RedGearCoreLoadingPlugin.util.getBoolean("SolidGlass")) || (transformedName.equals("net.minecraft.block.BlockIce") && RedGearCoreLoadingPlugin.util.getBoolean("SolidIce")))
        {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList isBlockSolidOnSide = new InsnList();

            isBlockSolidOnSide.add(new InsnNode(ICONST_1));
            isBlockSolidOnSide.add(new InsnNode(IRETURN));
            MethodNode mn = new MethodNode(ACC_PUBLIC, "isBlockSolidOnSide", "(Lnet/minecraft/world/World;IIILnet/minecraftforge/common/ForgeDirection;)Z", null, new String[] {});
            mn.instructions = isBlockSolidOnSide;
            node.methods.add(mn);
            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }

        return bytes;
    }
}
