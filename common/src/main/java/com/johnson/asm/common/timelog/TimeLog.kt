package com.johnson.asm.common.timelog

@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.LOCAL_VARIABLE
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class TimeLog