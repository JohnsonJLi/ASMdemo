package com.johnson.asm.timelog

import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.johnson.asm.timelog.helper.TimeLogClassNodeHelper
import com.kronos.plugin.base.BaseTransform
import com.kronos.plugin.base.ClassUtils
import com.kronos.plugin.base.Log
import com.kronos.plugin.base.TransformCallBack
import java.io.IOException

abstract class TimeLogTransform : Transform() {
    override fun getName(): String {
        return "TimeLogTransform"
    }

    override fun isIncremental(): Boolean {
        return true
    }

    @Throws(TransformException::class, InterruptedException::class, IOException::class)
    override fun transform(transformInvocation: TransformInvocation) {
        val injectHelper = TimeLogClassNodeHelper()
        val baseTransform = BaseTransform(transformInvocation, object : TransformCallBack {
            override fun process(className: String, classBytes: ByteArray?): ByteArray? {
                if (ClassUtils.checkClassName(className)) {
                    try {
                        return classBytes?.let { injectHelper.modifyClass(it) }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                return null
            }
        })
        baseTransform.startTransform()
    }

    override fun isCacheable(): Boolean {
        return true
    }
}