package com.johnson.asm.timelog

open class TimeLogConfig {

    @JvmOverloads
    fun transform(
        filterTime: Int = 16,
        injectAnnotationName: String = "com.johnson.asm.common.timelog.TimeLog"
    ) {
        ByteCodeInjectAnnotationName = injectAnnotationName.replace(".", "/")
        TimeLogConfig.filterTime = filterTime
    }

    companion object {

        @JvmField
        var ByteCodeInjectAnnotationName = ""

        //过滤时间
        @JvmField
        var filterTime = 16

    }
}
