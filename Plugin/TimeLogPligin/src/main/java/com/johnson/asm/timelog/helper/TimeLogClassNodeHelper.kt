package com.johnson.asm.timelog.helper

import com.johnson.asm.timelog.TimeLogConfig
import com.kronos.plugin.base.AsmHelper
import com.kronos.plugin.base.utils.isAbstractMethod
import com.kronos.plugin.base.utils.isNativeMethod
import com.kronos.plugin.base.utils.isStatic
import org.objectweb.asm.*
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

        classNode.methods?.filter {
            !it.isAbstractMethod && !it.isNativeMethod
        }?.forEach { method ->
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
        val isStaticMethod = isStatic

        val methodType: Type = Type.getMethodType(desc)


        var methodFirstLineNumber: Int? = null
        val labelStart = Label()
        val labelEnd = Label()
        instructions?.apply {
            val firstNode = first ?: return

            insertBefore(firstNode, LabelNode(Label()))
            insertBefore(
                firstNode,
                MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "java/lang/System",
                    "currentTimeMillis",
                    "()J",
                    false
                )
            )
            insertBefore(firstNode, VarInsnNode(LSTORE, 4321))
            insertBefore(firstNode, LabelNode(labelStart))

            iterator().forEach {
                if (methodFirstLineNumber == null && it is LineNumberNode) {
                    methodFirstLineNumber = it.line
//                    Log.info("LineNumberNode ${classNode.name}:${it.line}")
                }
                if ((it.opcode in IRETURN..RETURN) || it.opcode == ATHROW) {
                    // 2022/5/10 return  获取返回结果
                    if (printTimeLog) {
                        when (it.opcode) {
                            in IRETURN..DRETURN -> {
                                val returnType = methodType.returnType
                                val size = returnType.size
                                val descriptor = returnType.descriptor
                                if (size == 1) {
                                    //super.visitInsn(DUP)
                                    insertBefore(it, InsnNode(DUP))

                                } else {
                                    //super.visitInsn(DUP2)
                                    insertBefore(it, InsnNode(DUP2))
                                }
                                //val methodDesc = String.format("(%s)V", descriptor)
                                insertBefore(
                                    it,
                                    MethodInsnNode(
                                        INVOKESTATIC,
                                        "java/lang/String",
                                        "valueOf",
                                        String.format("(%s)Ljava/lang/String;", descriptor),
                                        false
                                    )
                                )
                                insertBefore(it, VarInsnNode(ASTORE, 6543))
                                //printValueOnStack(methodDesc)
                            }
                            ARETURN -> {
                                //super.visitInsn(DUP)
                                // printValueOnStack("(Ljava/lang/Object;)V")
                                insertBefore(it, InsnNode(DUP))
                                insertBefore(
                                    it,
                                    MethodInsnNode(
                                        INVOKESTATIC,
                                        "java/lang/String",
                                        "valueOf",
                                        "(Ljava/lang/Object;)Ljava/lang/String;",
                                        false
                                    )
                                )
                                insertBefore(it, VarInsnNode(ASTORE, 6543))
                            }
                            RETURN -> {
                                //printMessage("    return void")
                                insertBefore(it, LdcInsnNode("void"))
                                insertBefore(it, VarInsnNode(ASTORE, 6543))
                            }
                            else -> {
                                //printMessage("    abnormal return")
                                insertBefore(it, LdcInsnNode("abnormal"))
                                insertBefore(it, VarInsnNode(ASTORE, 6543))
                            }
                        }
                    }
                    //return end


                    insertBefore(it, LabelNode(Label()))
                    insertBefore(
                        it,
                        MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "java/lang/System",
                            "currentTimeMillis",
                            "()J",
                            false
                        )
                    )
                    insertBefore(it, VarInsnNode(LLOAD, 4321))
                    insertBefore(it, InsnNode(LSUB))
                    insertBefore(it, VarInsnNode(LSTORE, 5432))

                    var labelIFLE = Label()
                    if (!printTimeLog) {
                        insertBefore(it, VarInsnNode(LLOAD, 5432))
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
                    insertBefore(
                        it,
                        MethodInsnNode(
                            INVOKESPECIAL,
                            "java/lang/StringBuilder",
                            "<init>",
                            "()V",
                            false
                        )
                    )
                    insertBefore(it, LdcInsnNode("${classNode.name.replace("/", ".")}.$name >>> ["))
                    insertBefore(
                        it,
                        MethodInsnNode(
                            INVOKEVIRTUAL,
                            "java/lang/StringBuilder",
                            "append",
                            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                            false
                        )
                    )
                    insertBefore(it, VarInsnNode(LLOAD, 5432))
                    insertBefore(
                        it,
                        MethodInsnNode(
                            INVOKEVIRTUAL,
                            "java/lang/StringBuilder",
                            "append",
                            "(J)Ljava/lang/StringBuilder;",
                            false
                        )
                    )
                    insertBefore(
                        it,
                        LdcInsnNode("]ms  LineNumber : ${methodFirstLineNumber ?: -1}")
                    )
                    insertBefore(
                        it,
                        MethodInsnNode(
                            INVOKEVIRTUAL,
                            "java/lang/StringBuilder",
                            "append",
                            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                            false
                        )
                    )

                    if (printTimeLog) {
                        // 2022/5/10 arguments
                        var slotIndex = if (isStaticMethod) 0 else 1

                        val argumentTypes: Array<Type> = methodType.argumentTypes
                        if (argumentTypes.isNotEmpty()) {
                            insertBefore(it, LdcInsnNode("\narguments("))
                            insertBefore(
                                it,
                                MethodInsnNode(
                                    INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "append",
                                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                                    false
                                )
                            )

                            for (t in argumentTypes) {
                                val sort: Int = t.sort
                                val size: Int = t.size
                                val descriptor: String = t.descriptor
                                val opcode: Int = t.getOpcode(ILOAD)
                                insertBefore(
                                    it,
                                    LdcInsnNode("${if (slotIndex > (if (isStaticMethod) 0 else 1)) "\n          " else ""} arg$slotIndex : ")
                                )
                                insertBefore(
                                    it,
                                    MethodInsnNode(
                                        INVOKEVIRTUAL,
                                        "java/lang/StringBuilder",
                                        "append",
                                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                                        false
                                    )
                                )

                                //super.visitVarInsn(opcode, slotIndex)
                                insertBefore(it, VarInsnNode(opcode, slotIndex))

                                if (sort >= Type.BOOLEAN && sort <= Type.DOUBLE) {
//                            val methodDesc = String.format("(%s)V", descriptor)
//                            printValueOnStack(methodDesc)
                                    insertBefore(
                                        it,
                                        MethodInsnNode(
                                            INVOKEVIRTUAL,
                                            "java/lang/StringBuilder",
                                            "append",
                                            String.format(
                                                "(%s)Ljava/lang/StringBuilder;",
                                                descriptor
                                            ),
                                            false
                                        )
                                    )
                                } else {
//                            printValueOnStack("(Ljava/lang/Object;)V")
                                    insertBefore(
                                        it,
                                        MethodInsnNode(
                                            INVOKEVIRTUAL,
                                            "java/lang/StringBuilder",
                                            "append",
                                            String.format(
                                                "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
                                                descriptor
                                            ),
                                            false
                                        )
                                    )
                                }
                                slotIndex += size
                            }

                            insertBefore(it, LdcInsnNode(" ) \nreturn : "))
                            insertBefore(
                                it,
                                MethodInsnNode(
                                    INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "append",
                                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                                    false
                                )
                            )

                            // 2022/5/10 return log
                            insertBefore(it, VarInsnNode(ALOAD, 6543))
                            insertBefore(
                                it,
                                MethodInsnNode(
                                    INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "append",
                                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                                    false
                                )
                            )
                        }
                    }


                    insertBefore(
                        it,
                        MethodInsnNode(
                            INVOKEVIRTUAL,
                            "java/lang/StringBuilder",
                            "toString",
                            "()Ljava/lang/String;",
                            false
                        )
                    )

                    insertBefore(
                        it,
                        MethodInsnNode(
                            INVOKESTATIC,
                            "android/util/Log",
                            "e",
                            "(Ljava/lang/String;Ljava/lang/String;)I",
                            false
                        )
                    )
                    insertBefore(it, InsnNode(POP))

                    insertBefore(it, LabelNode(labelIFLE))

                }
            }
        }

        visitLabel(labelEnd)
        visitLocalVariable(variableName, "J", null, labelStart, labelEnd, 321)
    }
}


