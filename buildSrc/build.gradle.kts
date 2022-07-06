import org.gradle.kotlin.dsl.`kotlin-dsl`
plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = Plugin.Version.kotlin
}

object Plugin {
    object Version {
        const val spotless: String = "5.12.5"
        const val kotlin: String = "1.5.10"
        const val androidGradle: String = "7.1.2"
        const val navigation: String = "2.3.2"
        const val daggerHiltAndroid: String = "2.38.1"
    }
    const val spotless: String = "com.diffplug.spotless:spotless-plugin-gradle:${Version.spotless}"
    const val kotlin: String = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
    const val androidGradle: String = "com.android.tools.build:gradle:${Version.androidGradle}"
    const val navigationSafeArgs: String =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Version.navigation}"
    const val daggerHilt: String =
        "com.google.dagger:hilt-android-gradle-plugin:${Version.daggerHiltAndroid}"

}
dependencies {
    implementation(Plugin.spotless)
    implementation(Plugin.kotlin)
    implementation(Plugin.androidGradle)
    implementation(Plugin.navigationSafeArgs)
    implementation(Plugin.daggerHilt)
}
