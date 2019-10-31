import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

val app_name: String by project

android {
    compileSdkVersion(AndroidSettings.compileSdkVersion)

    defaultConfig {
        versionName = "0.0.1"
        versionCode = 1

        minSdkVersion(AndroidSettings.minSdkVersion)
        targetSdkVersion(AndroidSettings.targetSdkVersion)
        testInstrumentationRunner = AndroidSettings.testInstrumentationRunner

        vectorDrawables.useSupportLibrary = true

        base.archivesBaseName = "$versionName-$app_name"

        resValue("string", "app_name", app_name)
    }

    compileOptions {
        sourceCompatibility = AndroidSettings.sourceCompatibility
        targetCompatibility = AndroidSettings.targetCompatibility
    }
    packagingOptions {
        pickFirst("META-INF/services/javax.annotation.processing.Processor")
        exclude("META-INF/main.kotlin_module")
    }
    androidExtensions {
        isExperimental = true
    }
    testOptions {
        animationsDisabled = true
        unitTests(delegateClosureOf < Any ? > {
            unitTests.isReturnDefaultValues = true
            unitTests.isIncludeAndroidResources = true
        })
    }
    kotlinOptions {
        this as KotlinJvmOptions
        jvmTarget = "1.8"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
    implementation(project(":remote"))
    implementation(project(":cache"))
    implementation(Dependencies.appCompat)
    implementation(Dependencies.cardView)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.coroutinesAndroid)
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.glide)
    implementation(Dependencies.glideOkHttp)
    implementation(Dependencies.koinCore)
    implementation(Dependencies.koinViewModel)
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.materialDesign)
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.epoxy)

    androidTestImplementation(TestDependencies.assertJAndroid)
    androidTestImplementation(TestDependencies.espresso)
    androidTestImplementation(TestDependencies.espressoCont)
    androidTestImplementation(TestDependencies.koinTest)
    androidTestImplementation(TestDependencies.mockk)
    androidTestImplementation(TestDependencies.testRule)
    androidTestImplementation(TestDependencies.testRunner)
    kapt(Dependencies.glideCompiler)
    kapt(Dependencies.epoxyProcessor)
}
