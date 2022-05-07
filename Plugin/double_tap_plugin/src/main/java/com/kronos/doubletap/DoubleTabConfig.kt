package com.kronos.doubletap

open class DoubleTabConfig {
    var injectClassName = "com.johnson.asm.common.doubletap.DoubleTapCheck"
    var injectFunctionName = "isNotDoubleTap"
    var injectAnnotationName = "com.johnson.asm.common.doubletap.DoubleTap"
    fun transform() {
        ByteCodeInjectClassName = if (injectClassName.isEmpty()) {
            "com/johnson/asm/common/doubletap/DoubleTapCheck"
        } else {
            injectClassName.replace(".", "/")
        }
        ByteCodeInjectFunctionName = if (injectFunctionName.isEmpty()) {
            "isNotDoubleTap"
        } else {
            injectFunctionName
        }
        ByteCodeInjectAnnotationName = if (injectAnnotationName.isEmpty()) {
            "com/johnson/asm/common/doubletap/DoubleTap"
        } else {
            injectAnnotationName.replace(".", "/")
        }
    }

    companion object {
        @JvmField
        var ByteCodeInjectClassName = ""

        @JvmField
        var ByteCodeInjectFunctionName = ""

        @JvmField
        var ByteCodeInjectAnnotationName = ""
    }
}