package redgear.core.nbt

import net.minecraft.nbt.NBTBase
import scala.xml.NodeSeq
import scala.xml.Elem
import net.minecraft.nbt.JsonToNBT
import scala.xml.Text
import net.minecraft.nbt.NBTTagCompound
import scala.annotation.tailrec
import net.minecraft.nbt.NBTTagShort
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagLong
import net.minecraft.nbt.NBTTagByte
import net.minecraft.nbt.NBTTagString
import net.minecraft.nbt.NBTTagFloat
import net.minecraft.nbt.NBTTagDouble

object XmlToNbt {

  // NodeSeq to NBTTagCompound

  implicit def toNBT(nodes: Elem): NBTBase = {

    def recurse(node: Elem): (String, NBTBase) = {
      node.prefix match {
        case "byte" => (node.label, new NBTTagByte(node.text.toByte))
        case "short" => (node.label, new NBTTagShort(node.text.toShort))
        case "int" => (node.label, new NBTTagInt(node.text.toInt))
        case "long" => (node.label, new NBTTagLong(node.text.toLong))
        case "float" => (node.label, new NBTTagFloat(node.text.toFloat))
        case "double" => (node.label, new NBTTagDouble(node.text.toDouble))

        case "string" => (node.label, new NBTTagString(node.text))

        case _ => {
          val tag = new NBTTagCompound

          childRecurse(tag, (node \ ("_")) )
          return (node.label, tag)
        }
      }
    }

    @tailrec
    def childRecurse(tag: NBTTagCompound, child: NodeSeq) {
      if(!child.isInstanceOf[Elem])
        return
        
      val ans = recurse(child.head.asInstanceOf[Elem])

      tag.setTag(ans._1, ans._2)
      childRecurse(tag, NodeSeq fromSeq child.tail)
    }

    val tag = new NBTTagCompound
    childRecurse(tag, nodes)
    tag
  }

}