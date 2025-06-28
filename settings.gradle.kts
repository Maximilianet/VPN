pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // (опционально) если нужны плагины с JitPack
        maven(url = "https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")   // ← исправили здесь
    }
}

rootProject.name = "VPN"
include(":app")
