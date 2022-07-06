package com.johnson.asm.timelog

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.johnson.asm.timelog.TimeLogConfig
import com.kronos.plugin.base.Log
import org.gradle.api.Plugin
import org.gradle.api.Project

class TimeLogPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        project.extensions.create(EXT_NAME, TimeLogConfig::class.java)
        project.afterEvaluate { project1: Project ->
            var config = project1.extensions.findByName(EXT_NAME) as TimeLogConfig?
            if (config == null) {
                config = TimeLogConfig()
            }
            //timeLog
            config.transform()
        }
        Log.info(">>> TimeLogConfig enable : ${TimeLogConfig.enable} TimeLogConfig filterTime : ${TimeLogConfig.filterTime}")
        if (!TimeLogConfig.enable) return
        if (isApp) {
            val appExtension = project.extensions.getByType(
                AppExtension::class.java
            )
            appExtension.registerTransform(TimeLogAppTransform())
            return
        }
        if (project.plugins.hasPlugin("com.android.library")) {
            val libraryExtension = project.extensions.getByType(
                LibraryExtension::class.java
            )
            libraryExtension.registerTransform(TimeLogLibraryTransform())
        }
    }

    companion object {
        private const val EXT_NAME = "timeLog"
    }
}