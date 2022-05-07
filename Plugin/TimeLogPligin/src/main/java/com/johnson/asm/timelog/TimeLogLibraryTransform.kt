package com.johnson.asm.timelog

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet


class TimeLogLibraryTransform : TimeLogTransform() {

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return ImmutableSet.of(
                QualifiedContent.Scope.PROJECT
        )
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType>? {
        return TransformManager.CONTENT_CLASS
    }

}