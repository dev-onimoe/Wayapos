// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories.applyDefault()
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    }
}
allprojects {
    repositories.applyDefault()
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion(kotlinVersion)
            }
        }
    }
}
subprojects {
    applySpotless
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}