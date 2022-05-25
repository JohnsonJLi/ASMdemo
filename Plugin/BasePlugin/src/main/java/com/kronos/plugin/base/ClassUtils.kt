package com.kronos.plugin.base

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ClassUtils {
    fun path2Classname(entryName: String): String {
        // 感谢大佬 dingshaoran
        //ClassUtils.path2Classname(className); File.separator donot match jar entryName on windows
        return entryName.replace(".class", "")
            .replace('\\', '.')
            .replace('/', '.')
    }

    fun checkClassName(className: String): Boolean {
        if (className.contains("R\$")) {
            return false
        }
        if (className.endsWith("R.class")) {
            return false
        }
//        if (className.startsWith("com/chad/library")) {
//            return false
//        }
        return (!className.contains("R\\$") && !className.endsWith("R")
                && !className.endsWith("BuildConfig"))
    }

    fun saveFile(mTempDir: File?, modifiedClassBytes: ByteArray?): File? {
        val modified: File? = mTempDir
        modifiedClassBytes?.apply {
            if (mTempDir!!.exists()) {
                mTempDir.delete()
            }
            mTempDir.createNewFile()
            val stream = FileOutputStream(mTempDir)
            stream.use {
                stream.write(modifiedClassBytes)
            }
        }
        return modified
    }

//    /**
//     * 获取是否继承自某类
//     * @Author johnson
//     * @Date 2022/5/24 17:09
//     */
//    fun ClassReader.isSuper(superClassName: String): Boolean {
//        if (superClassName == className || superClassName == superName) return true
//        superName?.let {
//            ClassReader(superName).isSuper(superClassName)
//        }
//        return false
//    }
//
//    fun ClassNode.isSuper(superClassName: String): Boolean {
//        if (superClassName == name || superClassName == superName) return true
//        superName?.let {
//            ClassReader(superName).isSuper(superClassName)
//        }
//        return false
//    }
//
//
//    /**
//     * 检查当前类是 Object 类型
//     *
//     * @param className class name
//     * @return checked result
//     */
//    private fun isObject(className: String): Boolean {
//        return "java/lang/Object" == className
//    }
//
//    /**
//     * 判断是否实现了指定接口
//     *
//     * @param reader       class reader
//     * @param interfaceSet interface collection
//     * @return check result
//     */
//    fun hasImplSpecifiedInterfaces(reader: ClassReader, interfaceSet: Set<String>): Boolean {
//        return if (isObject(reader.className)) {
//            false
//        } else try {
//            if (containedTargetInterface(reader.interfaces, interfaceSet)) {
//                true
//            } else {
//                val parent = ClassReader(reader.superName)
//                hasImplSpecifiedInterfaces(parent, interfaceSet)
//            }
//        } catch (e: IOException) {
//            false
//        }
//    }
//
//    /**
//     * 检查接口及其父接口是否实现了目标接口
//     *
//     * @param interfaceList 待检查接口
//     * @param interfaceSet  目标接口
//     * @return checked result
//     * @throws IOException exp
//     */
//    @Throws(IOException::class)
//    private fun containedTargetInterface(
//        interfaceList: Array<String>,
//        interfaceSet: Set<String>
//    ): Boolean {
//        for (inter in interfaceList) {
//            if (interfaceSet.contains(inter)) {
//                return true
//            } else {
//                val reader = ClassReader(inter)
//                if (containedTargetInterface(reader.interfaces, interfaceSet)) {
//                    return true
//                }
//            }
//        }
//        return false
//    }

}