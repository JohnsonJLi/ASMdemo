package com.wallstreetcn.autotrack

import com.google.auto.service.AutoService
import com.kronos.plugin.base.PluginProvider
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @Author LiABao
 * @Since 2021/1/26
 */
@AutoService(value = [PluginProvider::class])
class AutoTrackPluginProvider : PluginProvider {

    override fun getPlugin(): Class<out Plugin<Project>> {
        return AutoTrackPlugin::class.java
    }

    override fun dependOn(): List<String> {
        return arrayListOf<String>().apply {
            // add("com.kronos.doubletap.DoubleTapProvider")
            //    add("com.kronos.plugin.thread.ThreadHookProvider")
        }
    }

}