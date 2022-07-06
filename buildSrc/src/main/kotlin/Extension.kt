import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.kotlin.dsl.apply
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

val PluginDependenciesSpec.androidApplication: PluginDependencySpec
    get() = id("com.android.application")
val PluginDependenciesSpec.androidLibrary: PluginDependencySpec
    get() = id("com.android.library")

val Project.applySpotless
    get() = apply(plugin = "spotless")

val PluginDependenciesSpec.kotlinLibrary: PluginDependencySpec
    get() = id("kotlin-library")
val PluginDependenciesSpec.javaLibrary: PluginDependencySpec
    get() = id("kotlin-library")
val PluginDependenciesSpec.kotlinAndroid: PluginDependencySpec
    get() = id("kotlin-android")
val PluginDependenciesSpec.kotlinAndroidExtensions: PluginDependencySpec
    get() = id("kotlin-android-extensions")
val PluginDependenciesSpec.daggerHiltAndroidPlugin: PluginDependencySpec
    get() = id("dagger.hilt.android.plugin")
val PluginDependenciesSpec.kotlinKapt: PluginDependencySpec
    get() = id("kotlin-kapt")
val PluginDependenciesSpec.androidxNavigationSafeargs: PluginDependencySpec
    get() = id("androidx.navigation.safeargs")

val PluginDependenciesSpec.kotlin: PluginDependencySpec
    get() = id("kotlin")

val PluginDependenciesSpec.safeArgs: PluginDependencySpec
    get() = id("androidx.navigation.safeargs.kotlin")

val PluginDependenciesSpec.parcelize: PluginDependencySpec
    get() = id("kotlin-parcelize")

fun RepositoryHandler.maven(url: String) {
    maven {
        setUrl(url)
    }
}

fun RepositoryHandler.applyDefault() {
    google()
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

fun DependencyHandler.implementAll(list: List<String>) {
    list.forEach {
        add("implementation", it)
    }
}

fun DependencyHandler.addPlugins(list: List<String>) {
    list.forEach {
        add(ScriptHandler.CLASSPATH_CONFIGURATION, it)
    }
}

fun DependencyHandler.kapt(dependencyNotation: String): Dependency? =
    add("kapt", dependencyNotation)
