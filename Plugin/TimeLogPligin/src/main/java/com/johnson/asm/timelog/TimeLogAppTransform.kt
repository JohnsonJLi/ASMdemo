package com.johnson.asm.timelog

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager


class TimeLogAppTransform : TimeLogTransform() {

    override fun getScopes(): MutableSet<QualifiedContent.ScopeType> {
        return mutableSetOf<QualifiedContent.ScopeType>().apply {
            addAll(TransformManager.SCOPE_FULL_PROJECT)
        }
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType>? {
        return TransformManager.CONTENT_JARS
    }

}