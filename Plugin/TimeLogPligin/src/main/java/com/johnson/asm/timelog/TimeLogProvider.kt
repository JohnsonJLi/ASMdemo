package com.johnson.asm.timelog

import com.google.auto.service.AutoService
import com.kronos.plugin.base.PluginProvider
import org.gradle.api.Plugin
import org.gradle.api.Project

@AutoService(value = [PluginProvider::class])
class TimeLogProvider : PluginProvider {
    override fun getPlugin(): Class<out Plugin<Project>> {
        return TimeLogPlugin::class.java
    }

    override fun dependOn(): List<String> {
        return arrayListOf<String>().apply {
//            add("com.kronos.doubletap.DoubleTapProvider")
//            add("com.kronos.plugin.thread.ThreadHookProvider")
//            add("com.wallstreetcn.autotrack.AutoTrackPluginProvider")
        }
    }

}