package com.johnson.asm.timelog

open class TimeLogConfig {

    var injectAnnotationName = "com.johnson.asm.common.timelog.TimeLog"
    var logFilterTime = 8
    var enable = false

    fun transform() {
        ByteCodeInjectAnnotationName = if (injectAnnotationName.isNullOrEmpty()) {
            "com/johnson/asm/common/timelog/TimeLog"
        } else {
            injectAnnotationName.replace(".", "/")
        }
        filterTime = logFilterTime
        TimeLogConfig.enable = this.enable
    }

    companion object {

        @JvmField
        var ByteCodeInjectAnnotationName = ""

        //过滤时间
        @JvmField
        var filterTime = 8

        @JvmField
        var enable = false
    }
}
