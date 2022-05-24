package com.johnson.asm.router

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.johnson.asm.router.helper.ScanRouterClassNodeHelper
import com.kronos.plugin.base.BaseTransform
import com.kronos.plugin.base.ClassUtils
import com.kronos.plugin.base.TransformCallBack
import java.io.IOException

class ScanRouterTransform : Transform() {
    override fun getName(): String {
        return "ScanRouterTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_JARS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return true
    }

    @Throws(TransformException::class, InterruptedException::class, IOException::class)
    override fun transform(transformInvocation: TransformInvocation) {
        val injectHelper = ScanRouterClassNodeHelper()
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