import Dependencies.AndroidX
import Dependencies.DI
import Dependencies.Network
import Dependencies.Test
import Dependencies.View

plugins {
    androidApplication
    kotlinAndroid
    kotlinAndroidExtensions
    kotlinKapt
    safeArgs
    daggerHiltAndroidPlugin
    id("org.jetbrains.kotlin.android")
}
android {
    defaultConfig {
        applicationId = Config.Android.applicationId
        minSdk = Config.Version.minSdkVersion
        targetSdk = Config.Version.targetSdkVersion
        buildToolsVersion = Config.Version.buildToolVersion
        versionCode = Config.Version.versionCode
        versionName = Config.Version.versionName
        compileSdk = Config.Version.compileSdkVersion
        testInstrumentationRunner = Config.Android.testInstrumentationRunner
        multiDexEnabled = Config.isMultiDexEnabled
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    androidTestImplementation(Test.androidXTest)
    androidTestImplementation(Test.espresso)
    androidTestImplementation(Test.espressoContrib)
    androidTestImplementation(Test.fragmentTesting)
    androidTestImplementation(Test.rules)
    androidTestImplementation(Test.archCoreTest)
    androidTestImplementation(Test.runner)
    testImplementation(Test.junit)
    testImplementation(Test.truth)
    implementAll(View.components)
    implementation(Network.moshi)
    implementation(DI.daggerHiltAndroid)
    implementation(DI.hiltViewModel)
    implementation(View.fragment)
    implementation(Dependencies.Utility.phoneNumberLib)
    with(View) {
        implementAll(components)
        implementation(fragment)
        implementation(materialComponent)
        implementation(constraintLayout)
        implementation(cardView)
        implementation(recyclerView)
        implementation(shimmerLayout)
        implementation(glide)
        implementation(progressBar)
        implementation(pinView)
        implementation(autoFormatEditText)
        implementation(swipeRefresh)
        implementation(circleimageview)
    }
    AndroidX.run {
        implementation(activity)
        implementation(coreKtx)
        implementation(navigationFragmentKtx)
        implementation(roomDatabaseRuntime)
        implementation(roomDatabaseCoroutines)
        kapt(roomDatabaseCompiler)
        implementation(navigationUiKtx)
        implementation(multiDex)
    }
    implementAll(Network.components)
    implementation(DI.hiltCore)
    kapt(DI.AnnotationProcessor.daggerHilt)
    kapt(DI.AnnotationProcessor.androidxHiltCompiler)
}
