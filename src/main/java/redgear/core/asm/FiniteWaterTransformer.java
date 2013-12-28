package redgear.core.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class FiniteWaterTransformer implements IClassTransformer 
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes){
        if (transformedName.equals("net.minecraft.block.BlockFlowing") && RedGearCoreLoadingPlugin.util.getBoolean("FiniteWater", false))
        {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList getSmallestFlowDecay = new InsnList();

            getSmallestFlowDecay.add(new VarInsnNode(ALOAD, 0));
            getSmallestFlowDecay.add(new InsnNode(ICONST_0));
            getSmallestFlowDecay.add(new FieldInsnNode(PUTFIELD , "net/minecraft/block/BlockFlowing", "field_72214_a", "I"));
            
            for(MethodNode method : node.methods)
            	if("func_72211_e".equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc)))
            		method.instructions.insertBefore(method.instructions.getFirst(), getSmallestFlowDecay);
            
            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }

        return bytes;
    }
}
