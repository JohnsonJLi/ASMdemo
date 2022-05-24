package com.johnson.asm.router

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.ArrayList

class RouterPlugin : Plugin<Project> {


    companion object {
        private const val EXT_NAME = "router-plugin"

        val routes = ArrayList<RouteMeta>()
    }

    override fun apply(project: Project) {
        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        project.extensions.create(EXT_NAME, RouterConfig::class.java)
        project.afterEvaluate { project1: Project ->
            var config = project1.extensions.findByName(EXT_NAME) as RouterConfig?
            if (config == null) {
                config = RouterConfig()
            }
            config.transform()
        }
        val extension = project.extensions.getByType(BaseAppModuleExtension::class.java)
        extension.registerTransform(ScanRouterTransform())
        extension.registerTransform(GenerateRouterTransform())
    }
}

data class RouteMeta(
    val type: Int, // 0:UNKNOWN  1:Activity 2:Fragment
    val destination: String,// Destination
    val path: String,// Path of route
)