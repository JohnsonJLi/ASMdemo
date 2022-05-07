package com.johnson.asm.timelog;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.LibraryExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class TimeLogPlugin implements Plugin<Project> {

    private static final String EXT_NAME = "timeLog";

    @Override
    public void apply(Project project) {
        boolean isApp = project.getPlugins().hasPlugin(AppPlugin.class);
        project.getExtensions().create(EXT_NAME, TimeLogConfig.class);
        project.afterEvaluate(project1 -> {
            TimeLogConfig config = (TimeLogConfig) project1.getExtensions().findByName(EXT_NAME);
            if (config == null) {
                config = new TimeLogConfig();
            }
            config.transform();
        });
        if (isApp) {
            AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
            appExtension.registerTransform(new TimeLogAppTransform());
            return;
        }
        if (project.getPlugins().hasPlugin("com.android.library")) {
            LibraryExtension libraryExtension = project.getExtensions().getByType(LibraryExtension.class);
            libraryExtension.registerTransform(new TimeLogLibraryTransform());
        }
    }
}
