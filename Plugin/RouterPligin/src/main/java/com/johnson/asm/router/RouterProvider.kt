package com.johnson.asm.router

import com.google.auto.service.AutoService
import com.kronos.plugin.base.PluginProvider
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @Author LiABao
 * @Since 2021/1/27
 */
@AutoService(value = [PluginProvider::class])
class RouterProvider : PluginProvider {
    override fun getPlugin(): Class<out Plugin<Project>> {
        return RouterPlugin::class.java
    }

    override fun dependOn(): List<String> {
        return arrayListOf<String>().apply {
//             add("com.kronos.plugin.thread.ThreadHookProvider")
//             add("com.johnson.asm.timelog.TimeLogProvider")
        }
    }

}