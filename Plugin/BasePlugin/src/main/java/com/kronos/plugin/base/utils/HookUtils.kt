package com.kronos.plugin.base.utils

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode

val MethodNode.nameWithDesc: String
    get() = name + desc

val MethodNode.isStatic: Boolean
    get() = access and Opcodes.ACC_STATIC != 0

val MethodNode.isInitMethod: Boolean
    get() = name == "<init>"