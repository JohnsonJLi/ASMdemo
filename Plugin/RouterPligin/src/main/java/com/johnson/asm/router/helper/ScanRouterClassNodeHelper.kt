package com.johnson.asm.router.helper

import com.johnson.asm.router.RouteMeta
import com.johnson.asm.router.RouterConfig
import com.johnson.asm.router.RouterPlugin
import com.kronos.plugin.base.AsmHelper
import com.kronos.plugin.base.Log
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ASM5
import org.objectweb.asm.tree.ClassNode
import java.io.IOException


class ScanRouterClassNodeHelper : AsmHelper {

    @Throws(IOException::class)
    override fun modifyClass(srcClass: ByteArray): ByteArray {
        val classNode = ClassNode(ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)

        classNode.isRouter()

        val classWriter = ClassWriter(0)
        //3  将classNode转为字节数组
        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }

    //是否是 路由
    private fun ClassNode.isRouter(): Boolean {
        val annotationName = String.format("L%s;", RouterConfig.routerAnnotationName)

        this.visibleAnnotations?.forEach { annotation ->
            if (annotation.desc == annotationName) {
                annotation.values?.forEach {
                    if (it is String && it != "router") {
                        Log.info(
                            ">>>>>>>>>> isRouter: $name  \n superName : $superName \n" +
                                    " sourceFile : $sourceFile \n" +
                                    " interfaces : $interfaces\n" +
                                    " router : $it"
                        )
                        RouterPlugin.routes.add(RouteMeta(1, name, it))
                    }
                }
                return true
            }
        }
        return false
    }

}


