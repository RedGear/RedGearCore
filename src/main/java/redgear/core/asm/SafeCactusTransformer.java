package redgear.core.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class SafeCactusTransformer  implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		
		if (transformedName.equals("net.minecraft.block.BlockCactus") && CoreLoadingPlugin.util.getBoolean("SafeCactus")){
			ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList onEntityCollidedWithBlock = new InsnList();
            
            LabelNode skip = new LabelNode();

            onEntityCollidedWithBlock.add(new VarInsnNode(ALOAD, 5));
            onEntityCollidedWithBlock.add(new MethodInsnNode(INVOKESTATIC, "redgear/core/asm/RedGearCore", "isItem", "(Lnet/minecraft/entity/Entity;)Z"));
            onEntityCollidedWithBlock.add(new JumpInsnNode(IFEQ, skip));
            onEntityCollidedWithBlock.add(new InsnNode(RETURN));
            onEntityCollidedWithBlock.add(skip);
            
            for(MethodNode method : node.methods)
            	if("func_149670_a".equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc)))
            		method.instructions.insertBefore(method.instructions.getFirst(), onEntityCollidedWithBlock);
            
            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }
		
		return bytes;
	}
}
