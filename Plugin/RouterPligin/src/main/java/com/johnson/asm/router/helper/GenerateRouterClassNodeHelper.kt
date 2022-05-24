package com.johnson.asm.router.helper

import com.johnson.asm.router.RouterConfig
import com.johnson.asm.router.RouterPlugin
import com.kronos.plugin.base.AsmHelper
import com.kronos.plugin.base.Log
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*
import java.io.IOException


class GenerateRouterClassNodeHelper : AsmHelper {

    @Throws(IOException::class)
    override fun modifyClass(srcClass: ByteArray): ByteArray {
        val classNode = ClassNode(ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)

        Log.info("GenerateRouterClassNodeHelper : ${classNode.name}")

        classNode.methods?.forEach all@{ method ->
            if (method.name == RouterConfig.injectRouterModuleName) {
                method.instructions?.apply {
                   iterator().forEach { insnNode ->
                       if ((insnNode.opcode in IRETURN..RETURN) || insnNode.opcode == ATHROW){
                           RouterPlugin.routes.forEach { routeMeta->
                               insertBefore(insnNode, LabelNode())
                               insertBefore(insnNode, VarInsnNode(ALOAD, 0))
                               insertBefore(insnNode, TypeInsnNode(NEW,"com/johnson/router/model/RouteMeta"))
                               insertBefore(insnNode, InsnNode(DUP))
                               insertBefore(insnNode, FieldInsnNode(GETSTATIC, "com/johnson/router/model/RouteType","ACTIVITY", "Lcom/johnson/router/model/RouteType;"))
                               insertBefore(insnNode, LdcInsnNode(Type.getType("L${routeMeta.destination};")))
                               insertBefore(insnNode, LdcInsnNode(routeMeta.path))
                               insertBefore(insnNode, MethodInsnNode(INVOKESPECIAL, "com/johnson/router/model/RouteMeta", "<init>", "(Lcom/johnson/router/model/RouteType;Ljava/lang/Class;Ljava/lang/String;)V", false))
                               insertBefore(insnNode, MethodInsnNode(INVOKESPECIAL, "com/johnson/router/LogisticsCenter\$Companion", "registerRoute", "(Lcom/johnson/router/model/RouteMeta;)V", false))
                               insertBefore(insnNode, LabelNode())
                           }

                           insertBefore(insnNode, LabelNode())
                           insertBefore(insnNode, InsnNode(ICONST_1))
                           insertBefore(insnNode, MethodInsnNode(INVOKESTATIC, "com/johnson/router/LogisticsCenter", "access\$setRegisterByPlugin\$cp", "(Z)V", false))
                           insertBefore(insnNode, LabelNode())
                       }
                    }

                }
            }
        }

        val classWriter = ClassWriter(0)
        //3  将classNode转为字节数组
        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }


}


