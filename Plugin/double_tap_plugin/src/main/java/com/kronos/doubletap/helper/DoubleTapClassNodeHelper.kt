package com.kronos.doubletap.helper

import com.kronos.doubletap.DoubleTabConfig
import com.kronos.plugin.base.AsmHelper
import com.kronos.plugin.base.Log
import com.kronos.plugin.base.asm.lambdaHelper
import com.kronos.plugin.base.utils.isInitMethod
import com.kronos.plugin.base.utils.isStatic
import com.kronos.plugin.base.utils.nameWithDesc
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.*
import java.io.IOException

class DoubleTapClassNodeHelper : AsmHelper {

    private val classNodeMap = hashMapOf<String, ClassNode>()

    @Throws(IOException::class)
    override fun modifyClass(srcClass: ByteArray): ByteArray {
        val classNode = ClassNode(ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)
        classNodeMap[classNode.name] = classNode
        val className = classNode.outerClass
        val parentNode = classNodeMap[className]

        classNode.interfaces?.forEach {
            DoubleTabConfig.hookPoints.forEach { point ->
                if (it == point.interfaceName) {
                    classNode.methods?.forEach { method ->
                        // 找到onClick 方法
                        if (method.isInitMethod) {
                            initFunction(classNode, method)
                        }
                        if (method.name == point.methodName && method.nameWithDesc == point.methodSign
                            && !method.isExcept()
                        ) {
                            insertTrack(classNode, method)
                        }
                    }
                }
            }
        }
        classNode.lambdaHelper(true) {
//            (it.name == "onClick" && it.desc.contains(")Landroid/view/View\$OnClickListener;"))
            DoubleTabConfig.hookPoints.forEach { point ->
                if (it.name == point.methodName && it.desc.contains(")${point.interfaceSignSuffix}")) {
                    return@lambdaHelper true
                }
            }
            return@lambdaHelper false
        }.apply {
            if (isNotEmpty()) {
                classNode.methods?.forEach { method ->
                    if (method.isInitMethod) {
                        forEach {
                            initFunction(classNode, method, it.name, it.isStatic)
                        }
                        return@forEach
                    }
                }
            }
        }.forEach { method ->
            insertTrack(classNode, method, method.isStatic)
        }
        val classWriter = ClassWriter(0)
        //3  将classNode转为字节数组
        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }

    private fun MethodNode.isExcept(): Boolean {
        val annotationName = String.format("L%s;", DoubleTabConfig.ByteCodeInjectAnnotationName)
        this.visibleAnnotations?.forEach { annotation ->
            if (annotation.desc == annotationName) {
                annotation.values.forEach {
                    if (it is Boolean) {
                        return it
                    }
                }
            }
        }
        return false
    }

    private fun insertLambda(node: ClassNode, method: MethodNode) {
        // 根据outClassName 获取到外部类的Node

    }

    private fun initFunction(
        node: ClassNode,
        method: MethodNode,
        methodName: String = "",
        isStatic: Boolean = false
    ) {
        var hasDoubleTap = false
        val variableName =
            if (isStatic) "doubleTapStatic_${methodName.replace("-", "_")}" else "doubleTap"
        node.fields?.forEach {
            if (it.name == variableName) {
                hasDoubleTap = true
            }
        }
        if (!hasDoubleTap) {
            val access = if (isStatic) {
                ACC_PRIVATE + ACC_FINAL + ACC_STATIC
            } else {
                ACC_PRIVATE + ACC_FINAL
            }
            node.visitField(
                access, variableName, String.format(
                    "L%s;",
                    DoubleTabConfig.ByteCodeInjectClassName
                ), node.signature, null
            )
            val instructions = method.instructions
            method.instructions?.iterator()?.forEach {
                if ((it.opcode >= Opcodes.IRETURN && it.opcode <= Opcodes.RETURN) || it.opcode == Opcodes.ATHROW) {
                    instructions.insertBefore(it, VarInsnNode(ALOAD, 0))
                    instructions.insertBefore(
                        it,
                        TypeInsnNode(NEW, DoubleTabConfig.ByteCodeInjectClassName)
                    )
                    instructions.insertBefore(it, InsnNode(DUP))
                    instructions.insertBefore(
                        it, MethodInsnNode(
                            INVOKESPECIAL, DoubleTabConfig.ByteCodeInjectClassName,
                            "<init>", "()V", false
                        )
                    )
                    instructions.insertBefore(
                        it, FieldInsnNode(
                            if (isStatic) PUTSTATIC else PUTFIELD, node.name, variableName,
                            String.format("L%s;", DoubleTabConfig.ByteCodeInjectClassName)
                        )
                    )
                }
            }
        }
    }


    private fun insertTrack(node: ClassNode, method: MethodNode, isStatic: Boolean = false) {

        val variableName =
            if (isStatic) "doubleTapStatic_${method.name.replace("-", "_")}" else "doubleTap"
        // 判断方法名和方法描述
        val instructions = method.instructions
        val firstNode = instructions.first
        instructions?.insertBefore(firstNode, LabelNode(Label()))
        instructions?.insertBefore(firstNode, VarInsnNode(ALOAD, 0))
        instructions?.insertBefore(
            firstNode, FieldInsnNode(
                if (isStatic) GETSTATIC else GETFIELD, node.name,
                variableName, String.format("L%s;", DoubleTabConfig.ByteCodeInjectClassName)
            )
        )
        var timeCheck: Int? = null
        val annotationName = String.format("L%s;", DoubleTabConfig.ByteCodeInjectAnnotationName)
        method.visibleAnnotations?.forEach forAnnotation@{ annotation ->
            if (annotation.desc == annotationName) {
                annotation.values.forEach {
                    if (it is Int) {
                        timeCheck = it
                        instructions?.insertBefore(
                            firstNode, IntInsnNode(
                                SIPUSH,
                                timeCheck!!
                            )
                        )
                        return@forAnnotation
                    }
                }
            }
        }

        instructions?.insertBefore(
            firstNode, MethodInsnNode(
                INVOKEVIRTUAL,
                DoubleTabConfig.ByteCodeInjectClassName,
                DoubleTabConfig.ByteCodeInjectFunctionName,
                if (timeCheck != null) "(I)Z" else "()Z",
                false
            )
        )
        val labelNode = LabelNode(Label())
        instructions?.insertBefore(firstNode, JumpInsnNode(IFNE, labelNode))
        instructions?.insertBefore(firstNode, InsnNode(RETURN))
        instructions?.insertBefore(firstNode, labelNode)
    }

}


