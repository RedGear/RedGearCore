package redgear.core.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class MinableTransformer implements IClassTransformer  {
	
	public static IGenCatcher catcher;

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		
		if(transformedName.equals("net.minecraft.world.gen.feature.WorldGenMinable") && RedGearCoreLoadingPlugin.util.getBoolean("GeocraftMinableHook")){
			ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);
            InsnList generateHook = new InsnList();
            LabelNode skip = new LabelNode();
            
            String minable = "net/minecraft/world/gen/feature/WorldGenMinable";
			
            generateHook.add(new VarInsnNode(ALOAD, 0));
            generateHook.add(new FieldInsnNode(GETFIELD , minable, "field_76542_a", "I"));
            generateHook.add(new VarInsnNode(ALOAD, 0));
            generateHook.add(new FieldInsnNode(GETFIELD , minable, "minableBlockMeta", "I"));
            generateHook.add(new VarInsnNode(ALOAD, 0));
            generateHook.add(new FieldInsnNode(GETFIELD , minable, "field_76541_b", "I"));
            generateHook.add(new VarInsnNode(ALOAD, 0));
            generateHook.add(new FieldInsnNode(GETFIELD , minable, "field_94523_c", "I"));
            generateHook.add(new MethodInsnNode(INVOKESTATIC, "redgear/core/asm/MinableTransformer", "generateHook", "(IIII)Z"));
            generateHook.add(new JumpInsnNode(IFNE, skip));
            generateHook.add(new InsnNode(ICONST_1));
            generateHook.add(new InsnNode(IRETURN));
            generateHook.add(skip);
            
            for(MethodNode method : node.methods)
            	if("func_76484_a".equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc)))
            		method.instructions.insertBefore(method.instructions.getFirst(), generateHook);

            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
		}
		
		return bytes;
	}
    
    public static boolean generateHook(int blockId, int blockMeta, int numberOfBlocks, int targetId){
    	if(catcher != null)
    		return catcher.checkForNew(blockId, blockMeta, numberOfBlocks, targetId);
    	else
    		return true;
    }
    
    public interface IGenCatcher{
    	public boolean checkForNew(int blockId, int blockMeta, int numberOfBlocks, int targetId);
    }

}
