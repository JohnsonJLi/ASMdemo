package com.johnson.asm.common.doubletap


/**
 * 点击仿重 注解
 * @param timeCheck 防重时间 取值 0~32767
 * @param except 是否忽略(true : 连点)
 * @Author johnson
 * @Date 2022/5/7 09:26
 */
@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.LOCAL_VARIABLE
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class DoubleTap(val timeCheck: Int = DoubleTapCheck.TIME_CHECK, val except: Boolean = false)