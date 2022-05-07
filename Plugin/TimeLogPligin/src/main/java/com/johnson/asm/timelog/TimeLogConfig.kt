package com.johnson.asm.timelog

open class TimeLogConfig {
    var injectAnnotationName = "com.johnson.asm.common.timelog.TimeLog"
    fun transform() {
        ByteCodeInjectAnnotationName = if (injectAnnotationName.isEmpty()) {
            "com/johnson/asm/common/timelog/TimeLog"
        } else {
            injectAnnotationName.replace(".", "/")
        }
    }

    companion object {

        @JvmField
        var ByteCodeInjectAnnotationName = ""

    }
}
