pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri(path = "https://androidx.dev/storage/compose-compiler/repository/") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri(path = "https://androidx.dev/storage/compose-compiler/repository/") }
    }
}

rootProject.name = "Rose Accounts Manager"
include(":app")
