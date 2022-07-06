import Dependencies.AndroidX.Version.roomDatabase
import Dependencies.View.Version.fragment

const val kotlinAndroid: String = "android"
const val kotlinKapt: String = "kapt"
const val ktLintVersion: String = "0.41.0"
const val kotlinVersion = "1.5.10"

object Config {
    object Version {
        const val minSdkVersion: Int = 22
        const val compileSdkVersion: Int = 31
        const val targetSdkVersion: Int = 31
        const val versionName: String = "1.0.0"
        const val versionCode: Int = 1
        const val buildToolVersion: String = "30.0.3"
    }

    const val isMultiDexEnabled: Boolean = true

    object Android {
        const val applicationId: String = "com.wayapaychat.wayapos"
        const val testInstrumentationRunner: String = "androidx.test.runner.AndroidJUnitRunner"
    }
}

interface Libraries {
    val components : List<String>
}

object Dependencies {
    object AndroidX : Libraries {
        object Version {
            const val coreKtx: String = "1.5.0"
            const val navigation: String = "2.3.5"
            const val multidex: String = "2.0.1"
            const val lifeCycle: String = "2.4.0-alpha01"
            const val activity: String = "1.3.0-alpha08"
            const val activityKtx: String = "1.3.0-alpha08"
            const val liveData: String = "2.3.1"
            const val roomDatabase: String = "2.4.2"
        }

        const val roomDatabaseRuntime = "androidx.room:room-runtime:$roomDatabase"
        const val roomDatabaseCoroutines = "androidx.room:room-ktx:$roomDatabase"
        const val roomDatabaseCompiler = "androidx.room:room-compiler:$roomDatabase"
        const val coreKtx: String = "androidx.core:core-ktx:${Version.coreKtx}"
        const val navigationFragmentKtx: String =
            "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
        const val navigationUiKtx: String =
            "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
        const val multiDex: String = "androidx.multidex:multidex:${Version.multidex}"
        const val activity: String = "androidx.activity:activity:${Version.activity}"
        const val lifeCycleCommon: String =
            "androidx.lifecycle:lifecycle-common-java8:${Version.lifeCycle}"
        const val viewModel: String =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifeCycle}"
        const val activityKtx = "androidx.activity:activity-ktx:${Version.activityKtx}"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.liveData}"
        override val components: List<String>
            get() = listOf(
                coreKtx,
                navigationFragmentKtx,
                navigationUiKtx,
                multiDex,
                activity,
                lifeCycleCommon,
                viewModel,
                activityKtx
            )
    }

    object View : Libraries {
        object Version {
            const val circleimageview = "3.1.0"
            const val materialComponent: String = "1.2.0-alpha04"
            const val shimmerLayout: String = "0.5.0"
            const val appCompat: String = "1.4.0-alpha01"
            const val constraintLayout: String = "2.0.4"
            const val fragment: String = "1.4.0-alpha01"
            const val cardView: String = "1.0.0"
            const val recyclerView: String = "1.2.0"
            const val glide: String = "4.11.0"
            const val pinView: String = "1.4.4"
            const val progressBar: String = "3.1.0"
            const val coreVersion: String = "1.3.2"
            const val lifecycleVersion: String = "2.3.1"
            const val autoFormatEditText : String = "0.9.3"
            const val swipeRefresh : String = "1.0.0"
        }

        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Version.swipeRefresh}"
        const val circleimageview = "de.hdodenhof:circleimageview:${Version.circleimageview}"
        const val progressBar: String =
            "com.mikhaellopez:circularprogressbar:${Version.progressBar}"
        const val glide: String = "com.github.bumptech.glide:glide:${Version.glide}"
        const val pinView: String = "io.github.chaosleung:pinview:${Version.pinView}"
        const val appCompat: String = "androidx.appcompat:appcompat:${Version.appCompat}"
        const val fragment: String = "androidx.fragment:fragment-ktx:${Version.fragment}"
        const val cardView: String = "androidx.cardview:cardview:${Version.cardView}"
        const val autoFormatEditText: String = "com.aldoapps:autoformatedittext:${Version.autoFormatEditText}"
        const val materialComponent: String =
            "com.google.android.material:material:${Version.materialComponent}"
        const val shimmerLayout: String =
            "com.facebook.shimmer:shimmer:${Version.shimmerLayout}"
        const val constraintLayout: String =
            "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
        const val recyclerView: String =
            "androidx.recyclerview:recyclerview:${Version.recyclerView}"
        override val components: List<String> = listOf(appCompat, fragment)
        const val androidCore :String = "androidx.core:core-ktx:${Version.coreVersion}"
        const val lifecycleRuntimeKtx :String = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycleVersion}"
        const val lifecycleViewModel :String = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycleVersion}"
        const val lifecycleLivedata :String = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycleVersion}"
        const val lifecycleViewModelSavedstate :String = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.lifecycleVersion}"
    }

    object FlowBinding {
        private const val flowBindingVersion: String = "1.0.0-alpha02"
        const val android: String =
            "io.github.reactivecircus.flowbinding:flowbinding-android:$flowBindingVersion"
        const val lifecycle: String =
            "io.github.reactivecircus.flowbinding:flowbinding-lifecycle:$flowBindingVersion"
    }

    object Network : Libraries {
        object Version {
            const val okhttp: String = "5.0.0-alpha.2"
            const val retrofit: String = "2.9.0"
            const val gsonConverter = "2.9.0"
            const val moshi: String = "1.9.2"
            const val gson = "2.8.6"
        }

        object AnnotationProcessor {
            const val moshi: String = "com.squareup.moshi:moshi-kotlin-codegen:${Version.moshi}"
        }

        const val gson = "com.google.code.gson:gson:${Version.gson}"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Version.gsonConverter}"
        private const val okhttp: String = "com.squareup.okhttp3:okhttp:${Version.okhttp}"
        private const val loggingInterceptor: String =
            "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"
        const val retrofit: String = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
        const val retrofitMoshi: String =
            "com.squareup.retrofit2:converter-moshi:${Version.retrofit}"
        const val moshi: String = "com.squareup.moshi:moshi-kotlin:${Version.moshi}"

        override val components: List<String> = listOf(
            okhttp,
            loggingInterceptor,
            retrofit,
            retrofitMoshi,
            moshi,
            gson,
            gsonConverter
        )
    }

    object DI {
        object Version {
            const val daggerHilt: String = "2.36"
            const val androidxHilt: String = "1.0.0-alpha02"
            const val hiltAndroidCompiler: String = "2.28-alpha"
        }

        object AnnotationProcessor {
            const val daggerHilt: String =
                "com.google.dagger:hilt-compiler:${Version.daggerHilt}"
            const val androidxHiltCompiler: String =
                "androidx.hilt:hilt-compiler:${Version.androidxHilt}"
            const val hiltAndroidCompiler =
                "com.google.dagger:hilt-android-compiler:${Version.hiltAndroidCompiler}"
        }

        const val daggerHiltAndroid: String =
            "com.google.dagger:hilt-android:${Version.daggerHilt}"

        const val hiltViewModel: String =
            "androidx.hilt:hilt-lifecycle-viewmodel:${Version.androidxHilt}"
        const val hiltTesting: String =
            "com.google.dagger:hilt-android-testing:${Version.daggerHilt}"
        const val hiltCore: String = "com.google.dagger:hilt-core:${Version.daggerHilt}"

    }

    object Coroutines : Libraries {
        object Version {
            const val coroutines: String = "1.5.0"
        }

        const val core: String =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
        const val android: String =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"

        override val components: List<String> = listOf(core, android)
    }

    object Cache {
        object Version {
            const val room: String = "2.3.0"
        }

        object AnnotationProcessor {
            const val room: String = "androidx.room:room-compiler:${Version.room}"
        }

        const val room: String = "androidx.room:room-ktx:${Version.room}"
    }

    object Performance {
        object Version {
            const val leakCanary: String = "2.5"
        }

        const val leakCanary: String =
            "com.squareup.leakcanary:leakcanary-android:${Version.leakCanary}"
    }

    object Utility{
        object Version{
            const val phonelibs : String = "8.12.18.1"
        }

        const val phoneNumberLib="com.lionscribe.open.libphonenumber:libphonenumber:${Version.phonelibs}"
    }


    object Test {
        object Version {
            const val junit: String = "4.13"
            const val runner: String = "1.2.0"
            const val rules: String = "1.3.0-rc03"
            const val testExt: String = "1.1.1"
            const val espresso: String = "3.3.0-rc03"
            const val truth: String = "1.0.1"
            const val mockWebServer: String = "4.7.2"
            const val robolectric: String = "4.5.1"
            const val archCoreTest: String = "1.1.1"
        }

        const val junit: String = "junit:junit:${Version.junit}"
        const val runner: String = "androidx.test:runner:${Version.runner}"
        const val fragmentTesting: String = "androidx.fragment:fragment-testing:$fragment"
        const val androidXTest: String = "androidx.test.ext:junit:${Version.testExt}"
        const val espresso: String = "androidx.test.espresso:espresso-core:${Version.espresso}"
        const val espressoContrib: String =
            "androidx.test.espresso:espresso-contrib:${Version.espresso}"
        const val rules: String = "androidx.test:rules:${Version.rules}"
        const val archCoreTest: String = "android.arch.core:core-testing:${Version.archCoreTest}"
        const val truth: String = "com.google.truth:truth:${Version.truth}"
        const val mockWebServer: String =
            "com.squareup.okhttp3:mockwebserver:${Version.mockWebServer}"
        const val coroutinesTest: String =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Coroutines.Version.coroutines}"
        const val robolectric: String = "org.robolectric:robolectric:${Version.robolectric}"
    }
}

object ProjectLib {
    const val app: String = ":app"
    const val core: String = ":core"
    const val remote: String = ":libraries:remote"
}
