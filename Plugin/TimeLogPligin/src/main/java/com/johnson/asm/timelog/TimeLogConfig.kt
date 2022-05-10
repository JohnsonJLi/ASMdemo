package com.johnson.asm.timelog

open class TimeLogConfig {

    var injectAnnotationName = "com.johnson.asm.common.timelog.TimeLog"
    var logFilterTime = 16

    fun transform() {
        ByteCodeInjectAnnotationName = if (injectAnnotationName.isNullOrEmpty()) {
            "com/johnson/asm/common/timelog/TimeLog"
        } else {
            injectAnnotationName.replace(".", "/")
        }
        filterTime = logFilterTime
    }

    companion object {

        @JvmField
        var ByteCodeInjectAnnotationName = ""

        //过滤时间
        @JvmField
        var filterTime = 16

    }
}
