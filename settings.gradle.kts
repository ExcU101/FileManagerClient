pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            val kotlinVersion = "1.5.30"
            val androidVersion = "7.1.0-alpha12"
            val hiltVersion = "2.38.1"

            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
                useVersion(kotlinVersion)
            }
            if (requested.id.id.startsWith("com.android")) {
                useModule("com.android.tools.build:gradle:$androidVersion")
            }
            if (requested.id.id.startsWith("dagger.hilt.android")) {
                useModule("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
            }
            if (requested.id.id.startsWith("org.jetbrains.kotlin.plugin.parcelize")) {
                useVersion(kotlinVersion)
            }
        }
    }
}
rootProject.name = "File Manager Client"
include(":app")