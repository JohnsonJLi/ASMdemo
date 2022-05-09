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

        @JvmField
        val hookPoints = listOf(
                DoubleTapHookPoint(
                        interfaceName = "android/view/View\$OnClickListener",
                        methodName = "onClick",
                        methodSign = "onClick(Landroid/view/View;)V"
                ),
                DoubleTapHookPoint(
                        interfaceName = "com/chad/library/adapter/base/listener/OnItemClickListener",
                        methodName = "onItemClick",
                        methodSign = "onItemClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V"
                ),
                DoubleTapHookPoint(
                        interfaceName = "com/chad/library/adapter/base/listener/OnItemChildClickListener",
                        methodName = "onItemChildClick",
                        methodSign = "onItemChildClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V"
                )
        )
    }
}

data class DoubleTapHookPoint(
        val interfaceName: String,
        val methodName: String,
        val methodSign: String
) {

    val interfaceSignSuffix = "L$interfaceName;"

}