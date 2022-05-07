package com.kronos.doubletap

open class DoubleTabConfig {
    var injectClassName = ""
    var injectFunctionName = ""
    fun transform() {
        if (injectClassName.isEmpty()) {
            ByteCodeInjectClassName = "com/johnson/asm/common/doubletap/DoubleTapCheck"
        } else {
            ByteCodeInjectClassName = injectClassName.replace(".", "/")
        }
        if (injectFunctionName.isEmpty()) {
            ByteCodeInjectFunctionName = "isNotDoubleTap"
        } else {
            ByteCodeInjectFunctionName = injectFunctionName
        }
    }

    companion object {
        @JvmField
        var ByteCodeInjectClassName = ""

        @JvmField
        var ByteCodeInjectFunctionName = ""
    }
}