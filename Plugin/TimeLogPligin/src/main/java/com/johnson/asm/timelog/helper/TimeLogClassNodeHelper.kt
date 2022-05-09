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
//        val className = classNode.outerClass
//        val parentNode = classNodeMap[className]

        classNode.methods?.forEach { method ->
            method.insertMethodTimeLogTrack(classNode)
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

    private fun MethodNode.insertMethodTimeLogTrack(classNode: ClassNode) {
        val variableName = "startTimeMillis"
        val printTimeLog = isPrintTimeLog()

        var methodFirstLineNumber: Int? = null
        val labelStart = Label()
        val labelEnd = Label()
        instructions?.apply {
            val firstNode = first ?: return
            insertBefore(firstNode, LabelNode(Label()))
            insertBefore(firstNode, MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false))
            insertBefore(firstNode, VarInsnNode(LSTORE, 321))
            insertBefore(firstNode, LabelNode(labelStart))

            iterator().forEach {
                if (methodFirstLineNumber == null && it is LineNumberNode) {
                    methodFirstLineNumber = it.line
//                    Log.info("LineNumberNode ${classNode.name}:${it.line}")
                }
                if ((it.opcode >= Opcodes.IRETURN && it.opcode <= Opcodes.RETURN) || it.opcode == Opcodes.ATHROW) {
                    insertBefore(it, LabelNode(Label()))
                    insertBefore(it, MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false))
                    insertBefore(it, VarInsnNode(LLOAD, 321))
                    insertBefore(it, InsnNode(LSUB))
                    insertBefore(it, VarInsnNode(LSTORE, 432))

                    var labelIFLE = Label()
                    if (!printTimeLog) {
                        insertBefore(it, VarInsnNode(LLOAD, 432))
                        insertBefore(it, IntInsnNode(BIPUSH, 8))
                        insertBefore(it, InsnNode(I2L))
                        insertBefore(it, InsnNode(LCMP))
                        labelIFLE = Label()
                        insertBefore(it, JumpInsnNode(IFLE, LabelNode(labelIFLE)))
                    }
                    insertBefore(it, LabelNode(Label()))

                    insertBefore(it, LdcInsnNode(if (printTimeLog) "TimeLog@" else "TimeLog"))
                    insertBefore(it, TypeInsnNode(NEW, "java/lang/StringBuilder"))
                    insertBefore(it, InsnNode(DUP))
                    insertBefore(it, MethodInsnNode(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false))
                    insertBefore(it, LdcInsnNode("${classNode.name.replace("/", ".")}.$name >>> ["))
                    insertBefore(it, MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false))
                    insertBefore(it, VarInsnNode(LLOAD, 432))
                    insertBefore(it, MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false))
                    insertBefore(it, LdcInsnNode("]ms  LineNumber : ${methodFirstLineNumber ?: -1}"))
                    insertBefore(it, MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false))
                    insertBefore(it, MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false))

                    insertBefore(it, MethodInsnNode(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false))
                    insertBefore(it, InsnNode(POP))

                    insertBefore(it, LabelNode(labelIFLE))

                }
            }
        }

        visitLabel(labelEnd)
        visitLocalVariable(variableName, "J", null, labelStart, labelEnd, 321)
    }
}


