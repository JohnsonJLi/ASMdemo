package com.johnson.asm.timelog

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.ImmutableSet


class TimeLogAppTransform : TimeLogTransform() {

    override fun getScopes(): MutableSet<QualifiedContent.ScopeType> {
        return ImmutableSet.of(QualifiedContent.Scope.PROJECT, QualifiedContent.Scope.SUB_PROJECTS)
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType>? {
        return TransformManager.CONTENT_CLASS
    }

}