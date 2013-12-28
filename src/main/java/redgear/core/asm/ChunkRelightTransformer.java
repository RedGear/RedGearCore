package redgear.core.asm;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.I2B;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ChunkRelightTransformer implements IClassTransformer {
	
	public static byte lightCheck = 0;

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		
		if (transformedName.equals("net.minecraft.world.chunk.Chunk") && RedGearCoreLoadingPlugin.util.getBoolean("ChunkRelightOnce"))
        {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList enqueueRelightChecks = new InsnList();
            
            LabelNode skip = new LabelNode();

            enqueueRelightChecks.add(new FieldInsnNode(GETSTATIC, "redgear/core/asm/ChunkRelightTransformer", "lightCheck", "B"));
            enqueueRelightChecks.add(new InsnNode(DUP));
            enqueueRelightChecks.add(new InsnNode(ICONST_1));
            enqueueRelightChecks.add(new InsnNode(IADD));
            enqueueRelightChecks.add(new InsnNode(I2B));
            enqueueRelightChecks.add(new FieldInsnNode(PUTSTATIC, "redgear/core/asm/ChunkRelightTransformer", "lightCheck", "B"));
            enqueueRelightChecks.add(new JumpInsnNode(IFNE, skip));
            enqueueRelightChecks.add(new InsnNode(RETURN));
            enqueueRelightChecks.add(skip);
            
            for(MethodNode method : node.methods)
            	if("func_76594_o".equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc)))
            		method.instructions.insertBefore(method.instructions.getFirst(), enqueueRelightChecks);
            
            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }
		
		
		
		
		
		
		return bytes;
	}

}
