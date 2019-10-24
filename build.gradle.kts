// Top-level build file where you can add configuration options common to all sub-projects/modules.
apply(plugin = "com.github.ben-manes.versions")

buildscript {
    repositories {
        google()
        jcenter()
        maven("https://kotlin.bintray.com/kotlinx/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidGradlePlugin}")
        classpath(kotlin("gradle-plugin", version = Versions.kotlin))
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.updatePluginVersion}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://kotlin.bintray.com/kotlinx/")
    }
    configurations.all {
        resolutionStrategy {
            force("org.objenesis:objenesis:2.6")
        }
    }
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}
