package com.johnson.asm.timelog.helper

import com.johnson.asm.timelog.TimeLogConfig
import com.kronos.plugin.base.AsmHelper
import com.kronos.plugin.base.Log
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.*
import java.io.IOException

class TimeLogClassNodeHelper : AsmHelper {

    private val classNodeMap = hashMapOf<String, ClassNode>()

    @Throws(IOException::class)
    override fun modifyClass(srcClass: ByteArray): ByteArray {
        val classNode = ClassNode(ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)
        classNodeMap[classNode.name] = classNode
        val className = classNode.outerClass
        Log.info("?> TimeLog name:${classNode.name}")
        val parentNode = classNodeMap[className]


        classNode.methods?.forEach { method ->
            if (method.isPrintTimeLog()) {
                method.insertMethodTimeLogTrack()
            }
        }


        val classWriter = ClassWriter(0)
        //3  将classNode转为字节数组
        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }


    //是否打印log
    private fun MethodNode.isPrintTimeLog(): Boolean {
        val annotationName = String.format("L%s;", TimeLogConfig.ByteCodeInjectAnnotationName)
        this.visibleAnnotations?.forEach { annotation ->
            if (annotation.desc == annotationName) {
                return true
            }
        }
        return false
    }


    private fun MethodNode.insertMethodTimeLogTrack() {
        var hasDoubleTap = false
        val variableName = "startTimeMillis"
        localVariables?.forEach {
            if (it.name == variableName) {
                hasDoubleTap = true
            }
        }

        if (!hasDoubleTap) {
            val labelStart = Label()
            val labelEnd = Label()
            val firstNode = instructions.first
            instructions?.insertBefore(firstNode, LabelNode(Label()))
            instructions?.insertBefore(firstNode, MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false))
            instructions?.insertBefore(firstNode, VarInsnNode(LSTORE, 321))
            instructions?.insertBefore(firstNode, LabelNode(labelStart))

            instructions?.iterator()?.forEach {
                if ((it.opcode >= Opcodes.IRETURN && it.opcode <= Opcodes.RETURN) || it.opcode == Opcodes.ATHROW) {
                    Log.info(">>  labelEnd  : ${it}")
                    instructions?.insertBefore(it, LabelNode(Label()))

                    instructions?.insertBefore(it, LdcInsnNode("TimeLog@"))
                    instructions?.insertBefore(it, MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false))
                    instructions?.insertBefore(it, VarInsnNode(LLOAD, 321))
                    instructions?.insertBefore(it, InsnNode(LSUB))
                    instructions?.insertBefore(it, MethodInsnNode(INVOKESTATIC, "java/lang/String", "valueOf", "(J)Ljava/lang/String;", false))
                    instructions?.insertBefore(it, MethodInsnNode(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false))
                    instructions?.insertBefore(it, InsnNode(POP))
                    instructions?.insertBefore(it, LabelNode(Label()))


                }
            }
            visitLabel(labelEnd)
            visitLocalVariable(variableName, "J", null, labelStart, labelEnd, 321)

        }
    }
}


